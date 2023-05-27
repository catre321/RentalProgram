package com.example.display;

import com.nguyenhoang.RentalSystem;
import com.nguyenhoang.client.Customer;
import com.nguyenhoang.stock.Item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;


public class Singleton {
    private static Singleton instance;

    public RentalSystem rentalSystem;
    public ObservableList<Item> itemObservableList;
    public ObservableList<Customer> customerObservableList;
    public ObservableList<Item> customerItemList;
    public String usernameLogIn;
    public String passwordLogIn;
    public String usernameCustomer;

    private Singleton() {
        rentalSystem = new RentalSystem();
        System.out.println(rentalSystem);
        itemObservableList = FXCollections.observableArrayList(rentalSystem.getItemsList());
        customerObservableList = FXCollections.observableArrayList(rentalSystem.getCustomersList());
    }
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    public int findItemIndex(int idNumber) {
        for (int i = 0; i < itemObservableList.size(); i++) {
            Item item = itemObservableList.get(i);
            if (item.getIdNumber() == idNumber) {
                return i;
            }
        }
        return -1;
    }
    public int findCustomerIndex(String username) {
        for (int i = 0; i < customerObservableList.size(); i++) {
            Customer customer = customerObservableList.get(i);
            if (customer.getUsername().equals(username)) {
                return i;
            }
        }
        System.out.println("Not found customer index");
        return -1;
    }

    public boolean setCustomerItemList() {
        ArrayList<Item> list = rentalSystem.getCustomerItemsList(usernameLogIn);
        if (list == null) {
            return false;
        } else {
            customerItemList = FXCollections.observableArrayList(list);
            return true;
        }
    }

    public FXMLLoader switch2Scene(String fxml, ActionEvent event) throws IOException {
        Stage stage;
        FXMLLoader fxmlLoader = new FXMLLoader(DisplayApplication.class.getResource(fxml));
        Parent root = fxmlLoader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        return fxmlLoader;
    }

    public void SuccessModalDialog(String fxml, String message) throws Exception {
        Stage dialogStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(DisplayApplication.class.getResource(fxml));
        Parent root = fxmlLoader.load();

        SuccessActionController successActionController = fxmlLoader.getController();
        successActionController.setMessage(message);

        Scene scene = new Scene(root);
        Image icon = new Image("Catfish.jpg");
        dialogStage.getIcons().add(icon);
        dialogStage.setTitle("The rental program");
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public void FailModalDialog(String fxml, String message) throws IOException {
        Stage dialogStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(DisplayApplication.class.getResource(fxml));
        Parent root = fxmlLoader.load();

        FailActionController failActionController = fxmlLoader.getController();
        failActionController.setErrorMessage(message);

        Scene scene = new Scene(root);
        Image icon = new Image("Catfish.jpg");
        dialogStage.getIcons().add(icon);
        dialogStage.setTitle("The rental program");
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
    
    public FXMLLoader modalDialog(String fxml) throws IOException {
        Stage dialogStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(DisplayApplication.class.getResource(fxml));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Image icon = new Image("Catfish.jpg");

        dialogStage.getIcons().add(icon);
        dialogStage.setTitle("The rental program - Group 22 - Individual");
        dialogStage.setScene(scene);
        dialogStage.show();

        return fxmlLoader;
    }
}
