package com.example.display;

import com.nguyenhoang.RentalSystem;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {
    public Button signInButton;
    Singleton singleton = Singleton.getInstance();
    @FXML
    private TextField inputUsername, inputPassword;
    @FXML
    private Label label;

    private RentalSystem rentalSystem = singleton.rentalSystem;
    private boolean isCustomer = false;

    public void initialize(String type) {
        if(type.equals("ADMIN"))  {
            isCustomer = false;
            signInButton.setVisible(false);
        } else {
            isCustomer = true;
            signInButton.setVisible(true);
        }
        System.out.println("Controller initialized!");
        label.setText("You currently log in as " + type + ", please enter username and password");
        label.setWrapText(true);
    }

    // button
    public void admin(ActionEvent event) throws Exception{
        System.out.println("Admin button clicked!");
        Controller controller = singleton.switch2Scene("LogIn.fxml", event).getController();
        controller.initialize("ADMIN");
    }

    public void customer(ActionEvent event) throws Exception{
        System.out.println("Customer button clicked!");
        Controller controller = singleton.switch2Scene("LogIn.fxml", event).getController();
        controller.initialize("CUSTOMER");
    }

    public void submitInput(ActionEvent event) throws Exception{
        try {
            singleton.usernameLogIn = inputUsername.getText();
            singleton.passwordLogIn = inputPassword.getText();
            System.out.println("Log in! button clicked!");
            System.out.println("username: " + singleton.usernameLogIn);
            System.out.println("password " + singleton.passwordLogIn);

            if(isCustomer){
                if (rentalSystem.checkLogInCustomer(singleton.usernameLogIn, singleton.passwordLogIn)) {
                    System.out.println("Log in success");
                    MenuController menuController = singleton.switch2Scene("Menu.fxml", event).getController();
                    menuController.initialize("customer");
                } else if (!rentalSystem.checkLogInCustomer(singleton.usernameLogIn, singleton.passwordLogIn)) {
                    throw new Exception("Log in fail, wrong username or password");
                }
            }
            else {
                if (rentalSystem.checkLogInAdmin(singleton.usernameLogIn, singleton.passwordLogIn)) {
                    System.out.println("Log in success");
                    MenuController menuController = singleton.switch2Scene("Menu.fxml", event).getController();
                    menuController.initialize("admin");
                } else if (!rentalSystem.checkLogInAdmin(singleton.usernameLogIn, singleton.passwordLogIn)) {
                    throw new Exception("Log in fail, wrong username or password");
                }
            }
        } catch (Exception error){
            singleton.FailModalDialog("FailAction.fxml", error.getMessage());
        }
    }

    public void addNewCustomer(ActionEvent event) throws Exception{
        System.out.println("Sign in button clicked!");
        singleton.modalDialog("NewCustomer.fxml");
    }

    public void backToLandingPage(ActionEvent event) throws Exception{
        System.out.println("Back button clicked!");
        singleton.switch2Scene("LandingPage.fxml", event);
    }

    public void handleEnterKeyPressed(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
            submitInput(actionEvent);
        }
    }
}