package com.example.display;

import com.nguyenhoang.client.Customer;
import com.nguyenhoang.setvalue.Enum;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PromoteCustomerController {
    Singleton singleton = Singleton.getInstance();
    @FXML
    private ComboBox<String> customerTypeComboBox;
    @FXML
    private Label label = new Label();
    private String customerUsername;
    private int index;

    public void closeButtonAction(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initialize(String username, String type){
        index = singleton.findCustomerIndex(username);
        this.customerUsername = username;
        String name = singleton.customerObservableList.get(index).getName();
        label.setText("Promote customer: " + name);
        customerTypeComboBox.getSelectionModel().select(type);
    }

    public void submitPromoteCustomer(ActionEvent event) throws Exception {
        try{
            String selectedOption = customerTypeComboBox.getValue();
            System.out.println("Selected option: " + selectedOption);
            if(selectedOption == null){
                throw new Exception("Please choose customer type!!!");
            }
            Customer customer = singleton.rentalSystem.promoteCustomer(customerUsername, Enum.AccountType.valueOf(selectedOption));
            if(customer == null){
                throw new Exception("Promote customer fail!!!");
            }
            if(!singleton.rentalSystem.writeCustomerToFile()){
                throw new Exception("Write file fail!!!");
            }
            singleton.customerObservableList.set(index, customer);
            singleton.SuccessModalDialog("SuccessAction.fxml", "Promote customer successfully!!!");
        } catch (Exception error) {
            singleton.FailModalDialog("FailAction.fxml", error.getMessage());
        }
    }
}
