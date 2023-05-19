package com.example.display;

import com.nguyenhoang.client.Customer;
import com.nguyenhoang.stock.DVD;
import com.nguyenhoang.stock.Item;
import com.nguyenhoang.stock.MovieRecord;
import com.nguyenhoang.setvalue.Enum;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayItemsv2Controller {
    Singleton singleton = Singleton.getInstance();
    private String type;
    @FXML
    private TextField searchField;
    @FXML
    private HBox useInDisplayItem;
    @FXML
    private Button newItemButton;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> idColumn;
    @FXML
    private TableColumn<Item, String> titleColumn;
    @FXML
    private TableColumn<Item, Enum.RentalType> rentalTypeColumn;
    @FXML
    private TableColumn<Item, Enum.LoanType> loanTypeColumn;
    @FXML
    private TableColumn<Item, Integer> numCopiesColumn;
    @FXML
    private TableColumn<Item, Double> rentalFeeColumn;
    @FXML
    private TableColumn<Item, Enum.Genre> genreColumn;
    @FXML
    private TableColumn<Item, String> rentalStatusColumn;
    private TableColumn<Item, Button> editColumn = new TableColumn<>("Edit");
    private TableColumn<Item, Button> deleteColumn = new TableColumn<>("Delete");
    private TableColumn<Item, Button> rentColumn = new TableColumn<>("Rent");
    private TableColumn<Item, Button> returnColumn = new TableColumn<>("Return");
    private TableColumn<Item, Button> forceReturnColumn = new TableColumn<>("Force return");


    public class TableCellButton extends TableCell<Item, Button> {
        private final Button button = new Button("Print");

        public TableCellButton(String type) {
            button.setText(type);
            switch (type){
                case "Edit":
                    button.setOnAction((ActionEvent event) -> {
                        Item item = getTableView().getItems().get(getIndex());
                        int id = item.getIdNumber();
                        button.setText(type);
                        NewItemController newItemController = null;
                        try {
                            newItemController = (singleton.modalDialog("NewItem.fxml")).getController();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        newItemController.initialize(id);

                    });
                    break;

                case "Delete":
                    button.setOnAction((ActionEvent event) -> {
                        Item item = getTableView().getItems().get(getIndex());
                        try {
                            singleton.rentalSystem.removeAnItem(item);
                            singleton.itemObservableList.remove(item);
                            System.out.println("Item has been deleted successfully");
                            if(!singleton.rentalSystem.writeItemToFile()){
                                throw new Exception("Item has NOT been saved successfully");
                            } else {
                                singleton.SuccessModalDialog("SuccessAction.fxml", "You have successfully delete an item in the list");
                            }
                        } catch (Exception error) {
                            try {
                                singleton.FailModalDialog("FailAction.fxml", error.getMessage());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    break;

                case "Rent":
                    button.setOnAction((ActionEvent event) -> {
                        int index = getIndex();
                        Item item = getTableView().getItems().get(index);
                        try {
                            singleton.rentalSystem.addToCustomerRentedItems(singleton.usernameLogIn, item);
                            System.out.println("Item has been rented successfully");
                            if(!singleton.rentalSystem.writeCustomerToFile()){
                                throw new Exception("Customer data has NOT saved successfully");
                            }
                            if(!singleton.rentalSystem.writeItemToFile()){
                                throw new Exception("Item data has NOT saved successfully");
                            }
                            System.out.println("Item has been rented successfully");

                            singleton.setCustomerItemList();
                            singleton.itemObservableList.set(index, item);
                            singleton.SuccessModalDialog("SuccessAction.fxml", "You have successfully rented an item in the list");
                        } catch (Exception error) {
                            try {
                                singleton.FailModalDialog("FailAction.fxml", error.getMessage());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    break;

                case "Return":
                    returnAnItem(singleton.usernameLogIn);
                    break;

                case "Force return":
                    returnAnItem(singleton.usernameCustomer);
                    break;
                default:
                    break;
            }
        }
        public void returnAnItem(String username){
            button.setOnAction((ActionEvent event) -> {
                int indexItem = getIndex();
                int indexCustomer = singleton.findCustomerIndex(username);

                Item item = getTableView().getItems().get(indexItem);
                try {
                    Customer customer = singleton.rentalSystem.removeFromCustomerRentedItems(username, item);
                    if(customer == null){
                        throw new Exception("Customer does not exist or it's have an error");
                    }
                    if(!singleton.rentalSystem.writeCustomerToFile()){
                        throw new Exception("Customer data has NOT saved successfully");
                    }
                    if(!singleton.rentalSystem.writeItemToFile()){
                        throw new Exception("Item data has NOT saved successfully");
                    }
                    System.out.println("Item has been returned successfully");
                    singleton.customerItemList.remove(item);
                    singleton.itemObservableList.set(indexItem, item);
                    singleton.customerObservableList.set(indexCustomer, customer);
                    singleton.SuccessModalDialog("SuccessAction.fxml", "You have successfully returned an item in the list");
                } catch (Exception error) {
                    try {
                        singleton.FailModalDialog("FailAction.fxml", error.getMessage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        @Override
        protected void updateItem(Button item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(button);
            }
        }
    }

    public void initialize(String type) {
        this.type = type;

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        rentalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalType"));
        loanTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loanType"));
        numCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("numCopies"));
        rentalFeeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalFee"));
        rentalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalType"));
        genreColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            switch (item.getRentalType()) {
                case DVD:
                    DVD dvd = (DVD) item;
                    return new SimpleObjectProperty<>(dvd.getGenre());
                case RECORD:
                    MovieRecord movie = (MovieRecord) item;
                    return new SimpleObjectProperty<>(movie.getGenre());
                default:
                    return new SimpleObjectProperty<>();
            }
        });
        rentalStatusColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item.getNumCopies() == 0) {
                return new SimpleObjectProperty<>("Not available");
            } else {
                return new SimpleObjectProperty<>("Available");
            }
        });
        switch(type){
            case "admin":
                editColumn.setCellFactory(column -> {
                    return new TableCellButton("Edit");
                });
                deleteColumn.setCellFactory(column -> {
                    return new TableCellButton("Delete");
                });
                itemTable.getColumns().addAll(editColumn, deleteColumn);
                itemTable.setItems(singleton.itemObservableList);
                break;

            case "customer":
                newItemButton.setVisible(false);
                rentColumn.setCellFactory(column -> {
                    return new TableCellButton("Rent");
                });
                itemTable.getColumns().add(rentColumn);
                itemTable.setItems(singleton.itemObservableList);
                break;

            case "customerRentedList":
                newItemButton.setVisible(false);
                returnColumn.setCellFactory(column -> {
                    return new TableCellButton("Return");
                });
                itemTable.getColumns().add(returnColumn);
                itemTable.setItems(singleton.customerItemList);
                break;
            default:
                break;
        }
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void showCustomerItems(String username) {
        useInDisplayItem.setVisible(false);

        ArrayList<Item> itemsList = new ArrayList<>();
        for(int i = 0; i < singleton.customerObservableList.size(); i++){
            if(singleton.customerObservableList.get(i).getUsername().equals(username)){
                itemsList = singleton.customerObservableList.get(i).getItems();
                break;
            }
        }

        singleton.customerItemList = FXCollections.observableArrayList(itemsList);
        itemTable.setItems(singleton.customerItemList);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        rentalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalType"));
        loanTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loanType"));
        numCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("numCopies"));
        rentalFeeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalFee"));
        rentalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalType"));
        genreColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            switch (item.getRentalType()) {
                case DVD:
                    DVD dvd = (DVD) item;
                    return new SimpleObjectProperty<>(dvd.getGenre());
                case RECORD:
                    MovieRecord movie = (MovieRecord) item;
                    return new SimpleObjectProperty<>(movie.getGenre());
                default:
                    return new SimpleObjectProperty<>();
            }
        });
        rentalStatusColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item.getNumCopies() == 0) {
                return new SimpleObjectProperty<>("Not available");
            } else {
                return new SimpleObjectProperty<>("Available");
            }
        });
        forceReturnColumn.setCellFactory(column -> {
            return new TableCellButton("Force return");
        });

        itemTable.getColumns().add(forceReturnColumn);
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void showAllOutOfStockItem() {
        System.out.println("Show all out of stock item pressed");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();

        for (int i = 0; i < singleton.itemObservableList.size(); i++) {
            if (singleton.itemObservableList.get(i).getNumCopies() <= 0) {
                filteredItems.add(singleton.itemObservableList.get(i));
            }
        }

        itemTable.setItems(filteredItems);
        switch(type){
            case "admin":
                editColumn.setCellFactory(column -> {
                    return new TableCellButton("Edit");
                });
                deleteColumn.setCellFactory(column -> {
                    return new TableCellButton("Delete");
                });
                break;

            case "customer":
                rentColumn.setCellFactory(column -> {
                    return new TableCellButton("Rent");
                });
                break;

            case "customerRentedList":
                returnColumn.setCellFactory(column -> {
                    return new TableCellButton("Return");
                });
                break;
            default:
                break;
        }
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void searchAction() {
        String searchText = searchField.getText();
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();

        for (int i = 0; i < singleton.itemObservableList.size(); i++) {
            if (singleton.itemObservableList.get(i).getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredItems.add(singleton.itemObservableList.get(i));
            }
        }

        if (searchText == null || searchText.trim().isEmpty()) {
            switch(type){
                case "admin":
                    itemTable.setItems(singleton.itemObservableList);
                    editColumn.setCellFactory(column -> {
                        return new TableCellButton("Edit");
                    });
                    deleteColumn.setCellFactory(column -> {
                        return new TableCellButton("Delete");
                    });
                    break;

                case "customer":
                    newItemButton.setVisible(false);
                    itemTable.setItems(singleton.itemObservableList);
                    rentColumn.setCellFactory(column -> {
                        return new TableCellButton("Rent");
                    });
                    break;

                case "customerRentedList":
                    useInDisplayItem.setVisible(false);
                    itemTable.setItems(singleton.customerItemList);
                    returnColumn.setCellFactory(column -> {
                        return new TableCellButton("Return");
                    });
                    break;
                default:
                    break;
            }

        } else {
            itemTable.setItems(filteredItems);

            switch(type){
                case "admin":
                    editColumn.setCellFactory(column -> {
                        return new TableCellButton("Edit");
                    });
                    deleteColumn.setCellFactory(column -> {
                        return new TableCellButton("Delete");
                    });
                    break;

                case "customer":
                    rentColumn.setCellFactory(column -> {
                        return new TableCellButton("Rent");
                    });
                    break;

                case "customerRentedList":
                    returnColumn.setCellFactory(column -> {
                        return new TableCellButton("Return");
                    });
                    break;
                default:
                    break;
            }
        }
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Clear out filteredItems when search text is empty
        if (searchText.trim().isEmpty()) {
            filteredItems.clear();
        }
    }

    public void handleEnterKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            searchAction();
        }
    }

    public void newItemAction(ActionEvent event) throws IOException {
        singleton.modalDialog("NewItem.fxml");
    }


//    public void searchAction() {
//        String searchText = searchField.getText();
//        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
//
//        for (int i = 0; i < singleton.itemObservableList.size(); i++) {
//            if (singleton.itemObservableList.get(i).getTitle().toLowerCase().contains(searchText.toLowerCase())) {
//                filteredItems.add(singleton.itemObservableList.get(i));
//            }
//        }
//        // update the table with the filtered items
////        itemTable.getItems().clear();
////        itemTable.getItems().addAll(filteredItems);
//        SortedList<Item> sortedItems = new SortedList<>(filteredItems);
//        sortedItems.comparatorProperty().bind(itemTable.comparatorProperty());
//        itemTable.setItems(filteredItems);
//    }

}
