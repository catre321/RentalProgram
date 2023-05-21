package com.example.display;

import com.nguyenhoang.RentalSystem;
import com.nguyenhoang.client.Customer;
import com.nguyenhoang.setvalue.Enum;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class NewCustomerController {
    private Singleton singleton = Singleton.getInstance();
    @FXML
    private Label label;
    @FXML
    private TextField inputName, inputAddress, inputPhone, inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private Button submitButton;
    private String customerId;
    private int index;

    public void initialize() {
        System.out.println("Initialize new customer");
    }
    public void initialize(String username) {
        System.out.println("Initialize update customer");
        this.label.setText("Update customer");

        this.index = this.singleton.findCustomerIndex(username);
        Customer customer= singleton.customerObservableList.get(index);

        this.customerId = customer.getId();
        inputName.setText(customer.getName());
        inputAddress.setText(customer.getAddress());
        inputPhone.setText(customer.getPhone());
        inputUsername.setText(customer.getUsername());
        inputPassword.setText(customer.getPassword());
        submitButton.setOnAction(event -> {
            this.submitUpdateCustomer(event);
        });
    }

    private void submitUpdateCustomer(ActionEvent event) {
        System.out.println("Submit update customer has been pressed");
        try {
            checkInput();
            String name = inputName.getText();
            String address = inputAddress.getText();
            String phone = inputPhone.getText();
            String username = inputUsername.getText();
            String password = inputPassword.getText();

            Customer updatedCustomer = singleton.rentalSystem.updateCustomer(customerId, name, address, phone, username, password);
            System.out.println("An customer has been SUCCESSFULLY updated");
            if (!singleton.rentalSystem.writeCustomerToFile()) {
                throw new Exception("An customer has NOT been saved to file successfully");
            }

            System.out.println("An customer has been SUCCESSFULLY saved to file");
            singleton.customerObservableList.set(index, updatedCustomer);
            singleton.SuccessModalDialog("SuccessAction.fxml",
                    "You have successfully update acustomer");
            singleton.switch2Scene("LandingPage.fxml", event);
        } catch (Exception e) {
            try {
                singleton.FailModalDialog("FailAction.fxml", e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void checkInput() throws Exception {
        if (inputName.getText().isEmpty() || inputAddress.getText().isEmpty() || inputPhone.getText().isEmpty() ||
                inputUsername.getText().isEmpty() || inputPassword.getText().isEmpty()) {
            throw new Exception("You have NOT filled in all the information");
        }
    }

    public void submitNewCustomer(ActionEvent event) {
        System.out.println("Submit new customer has been pressed");
        try {
            checkInput();

            int customerIdNumber = singleton.rentalSystem.getHighestCustomerId();
            customerIdNumber++;

            String id = String.format("C%03d", customerIdNumber);
            String name = inputName.getText();
            String address = inputAddress.getText();
            String phone = inputPhone.getText();
            String username = inputUsername.getText();
            String password = inputPassword.getText();

            Enum.AccountType accountType = Enum.AccountType.GUEST;

            Customer newCustomer = singleton.rentalSystem.addCustomer(id, name, address, phone, accountType, username, password);
            System.out.println("An customer has been SUCCESSFULLY added");
            if (!singleton.rentalSystem.writeCustomerToFile()) {
                throw new Exception("An customer has NOT been saved to file successfully");
            }

            System.out.println("An customer has been SUCCESSFULLY saved to file");
            singleton.customerObservableList.add(newCustomer);
            singleton.SuccessModalDialog("SuccessAction.fxml",
                    "You have successfully sign up a new customer");

        } catch (Exception e) {
            try {
                singleton.FailModalDialog("FailAction.fxml", e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void closeButtonAction(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}