package com.example.display;

import com.nguyenhoang.client.Customer;
import com.nguyenhoang.client.VIPAccount;
import com.nguyenhoang.setvalue.Enum;

import com.nguyenhoang.stock.DVD;
import com.nguyenhoang.stock.Item;
import com.nguyenhoang.stock.MovieRecord;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayCustomersController {
    private Singleton singleton = Singleton.getInstance();
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox <String> groupComboBox;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, Integer> totalSuccessfulRentalColumn;
    @FXML
    private TableColumn<Customer, Enum.AccountType> accountTypeColumn;
    @FXML
    private TableColumn<Customer, Integer> rewardPointColumn;
    private TableColumn<Customer, Button> promoteColumn = new TableColumn<>("Promote");
    private TableColumn<Customer, Button> showAllRentedColumn = new TableColumn<>("Show all rented");
    private TableColumn<Customer, Button> getRewardColumn = new TableColumn<>("Get reward");

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public class TableCellButton extends TableCell<Customer, Button> {
        private final Button button = new Button("Print");

        public TableCellButton(String type) {
            button.setText(type);
            switch (type){
                case "Show All Rented":
                    button.setOnAction((ActionEvent event) -> {
                        Customer customer = getTableView().getItems().get(getIndex());
                        singleton.usernameCustomer = customer.getUsername();

                        try {
                            DisplayItemsv2Controller displayItemsv2Controller = (singleton.modalDialog("DisplayItemsv2.fxml")).getController();
                            displayItemsv2Controller.showCustomerItems(customer.getUsername());

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case "Promote":
                    button.setOnAction((ActionEvent event) -> {
                        Customer customer = getTableView().getItems().get(getIndex());
                        try {
                            PromoteCustomerController promoteCustomerController = (singleton.modalDialog("PromoteCustomer.fxml")).getController();
                            promoteCustomerController.initialize(customer.getUsername(), customer.getAccountType().toString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case "Get reward":
                    button.setOnAction((ActionEvent event) -> {
                        int index = getIndex();
                        Customer customer = getTableView().getItems().get(index);
                        try {
                            customer = singleton.rentalSystem.getReward(customer.getId());

                            if(customer == null){
                                throw new Exception("Error when giving free rental!");
                            }
                            if(!singleton.rentalSystem.writeCustomerToFile()){
                                throw new Exception("Customers data has NOT saved successfully");
                            }
                            System.out.println("-100 to reward point successfully");

                            singleton.customerObservableList.set(index, customer);
                            singleton.SuccessModalDialog("SuccessAction.fxml", "You have successfully giving a free rental");
                        } catch (Exception error) {
                            try {
                                singleton.FailModalDialog("FailAction.fxml", error.getMessage());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void updateItem(Button customer, boolean empty) {
            super.updateItem(customer, empty);

            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(button);
            }
        }
    }

    public void initialize() {
        customerTable.setItems(singleton.customerObservableList);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        totalSuccessfulRentalColumn.setCellValueFactory(new PropertyValueFactory<>("totalSuccessfulRental"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        rewardPointColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue();
            if(customer.getAccountType() == Enum.AccountType.VIP){
                VIPAccount vipAccount = (VIPAccount) customer;
                return new SimpleObjectProperty<>(vipAccount.getRewardPoint());
            }
            return new SimpleObjectProperty<>();
        });
        showAllRentedColumn.setCellFactory(column ->{
            return new TableCellButton("Show All Rented");
        });
        promoteColumn.setCellFactory(column ->{
            return new TableCellButton("Promote");
        });
        getRewardColumn.setCellFactory(column -> {
            return new TableCellButton("Get reward");
        });
        customerTable.getColumns().addAll(showAllRentedColumn, promoteColumn, getRewardColumn);
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void searchAction() {
        String searchText = searchField.getText();
        ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

        for (int i = 0; i < singleton.customerObservableList.size(); i++) {
            if (singleton.customerObservableList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredCustomers.add(singleton.customerObservableList.get(i));
            }
        }

        if(searchText.trim().isEmpty()){
            customerTable.setItems(singleton.customerObservableList);
        } else {
            customerTable.setItems(filteredCustomers);
        }

        showAllRentedColumn.setCellFactory(column ->{
            return new TableCellButton("Show All Rented");
        });
        promoteColumn.setCellFactory(column ->{
            return new TableCellButton("Promote");
        });
        getRewardColumn.setCellFactory(column -> {
            return new TableCellButton("Get reward");
        });
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void handleEnterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource() == searchField) {
                searchAction();
            } else if (event.getSource() == groupComboBox) {
                showAGroupOfCustomerAction();
            }
        }
    }

    public void showAGroupOfCustomerAction(){
        try {
            System.out.println("Search group of customer pressed");
            groupComboBox.getValue();
            if (groupComboBox == null){
                throw new Exception("Input being left blank!!!");
            }

            ArrayList<Customer> foundCustomer = singleton.rentalSystem.searchCustomerGroup(Enum.AccountType.valueOf(groupComboBox.getValue()));
            ObservableList<Customer> foundCustomerObservableList = FXCollections.observableArrayList(foundCustomer);

            customerTable.setItems(foundCustomerObservableList);
        } catch (Exception error) {
            try {
                singleton.FailModalDialog("FailAction.fxml", error.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
