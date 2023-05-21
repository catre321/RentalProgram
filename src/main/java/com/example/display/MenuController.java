package com.example.display;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class MenuController {
    private Singleton singleton = Singleton.getInstance();
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label label;


    public void initialize(String userType) throws Exception {
        if(userType.equals("admin")) {
            label.setText("Welcome admin to the rental program: " + singleton.usernameLogIn);
            comboBox.getItems().addAll( "DISPLAY all items in store", "DISPLAY all customers");
        } else if(userType.equals("customer")) {
            label.setText("Welcome customer to the rental program: " + singleton.usernameLogIn);
            comboBox.getItems().addAll("UPDATE customer data", "RENT an item", "RETURN an item");
            if (!singleton.setCustomerItemList()){
                throw new Exception("Cannot find customer item list");
            }
        }
    }

    public void submitOption(ActionEvent event) throws Exception {
        try {
            String selectedOption = comboBox.getValue();
            if (selectedOption == null) {
                throw new Exception("No option selected");
            }
            System.out.println("Selected option: " + selectedOption);

            switch (selectedOption) {
                //admin part
                case "DISPLAY all items in store":
                    System.out.println("DISPLAY all items in store being selected");
                    DisplayItemsv2Controller displayAllItemsv2Controller = (singleton.modalDialog("DisplayItemsv2.fxml")).getController();
                    displayAllItemsv2Controller.initialize("admin");
                    break;

                case "DISPLAY all customers":
                    System.out.println("DISPLAY all customers being selected");
                    singleton.modalDialog("DisplayCustomers.fxml");
                    break;

                //customer part
                case "UPDATE customer data":
                    System.out.println("UPDATE customer data being selected");
                    NewCustomerController newCustomerController = (singleton.switch2Scene("NewCustomer.fxml", event)).getController();
                    newCustomerController.initialize(singleton.usernameLogIn);
                    break;

                case "RENT an item":
                    System.out.println("RENT an item being selected");
                    DisplayItemsv2Controller displayAllItemsv2ControllerCustomer = (singleton.modalDialog("DisplayItemsv2.fxml")).getController();
                    displayAllItemsv2ControllerCustomer.initialize("customer");
                    break;

                case "RETURN an item":
                    System.out.println("RETURN an item being selected");
                    DisplayItemsv2Controller displayAllItemsv2ControllerCustomerReturn = (singleton.modalDialog("DisplayItemsv2.fxml")).getController();
                    displayAllItemsv2ControllerCustomerReturn.initialize("customerRentedList");
                    break;
            }
        } catch (Exception error) {
            try {
                singleton.FailModalDialog("FailAction.fxml", error.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void goBack2LogIn(ActionEvent event) throws Exception {
        singleton.switch2Scene("LandingPage.fxml", event);
    }

    public void handleEnterKeyPressed(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
            submitOption(actionEvent);
        }
    }
}

