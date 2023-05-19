package com.example.display;

import com.nguyenhoang.RentalSystem;
import com.nguyenhoang.setvalue.Enum;
import com.nguyenhoang.stock.DVD;
import com.nguyenhoang.stock.Item;
import com.nguyenhoang.stock.MovieRecord;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;


public class NewItemController {
    Singleton singleton = Singleton.getInstance();
    private RentalSystem rentalSystem = singleton.rentalSystem;
    private int itemIdNumber;
    private int index;
    @FXML
    private Label label;
    @FXML
    private TextField inputYearPublished, inputTitle, inputNumCopies, inputFee;
    @FXML
    private ComboBox<String> rentalTypeComboBox, genreComboBox,loanTypeComboBox;
    @FXML
    private Button submit;

    public void initialize() {
        System.out.println("Initialize new item");
    }

    public void initialize(int itemIdNumber) {
        System.out.println("Initialize update item");
        this.label.setText("Update an item");
        this.index = this.singleton.findItemIndex(itemIdNumber);

        this.itemIdNumber = itemIdNumber;
        Item item = singleton.itemObservableList.get(index);

        inputYearPublished.setText(item.getYearPublished());
        inputTitle.setText(item.getTitle());
        inputNumCopies.setText(String.valueOf(item.getNumCopies()));
        inputFee.setText(String.valueOf(item.getRentalFee()));
        rentalTypeComboBox.getSelectionModel().select(item.getRentalType().toString());
        if(item.getRentalType().equals(Enum.RentalType.RECORD)){
            MovieRecord updateRecord = (MovieRecord)item;
            genreComboBox.getSelectionModel().select(updateRecord.getGenre().toString());
        } else if(item.getRentalType().equals(Enum.RentalType.DVD)){
            DVD updateDVD = (DVD)item;
            genreComboBox.getSelectionModel().select(updateDVD.getGenre().toString());
        }
        loanTypeComboBox.getSelectionModel().select(item.getLoanType().toString());
        submit.setOnAction(event -> {
            this.submitUpdateItem(event);
        });
    }
    public void checkInput() throws Exception {
        if (inputYearPublished.getText().isEmpty() || inputTitle.getText().isEmpty() || inputNumCopies.getText().isEmpty()
                || inputFee.getText().isEmpty() || rentalTypeComboBox.getValue().isEmpty() || loanTypeComboBox.getValue().isEmpty() ) {
            throw new Exception("You have NOT filled in all the information");
        } else {
            Enum.RentalType rentalType = Enum.RentalType.valueOf(rentalTypeComboBox.getValue());
            if (rentalType == Enum.RentalType.RECORD || rentalType == Enum.RentalType.DVD) {
                if (genreComboBox.getValue() == null) {
                    throw new Exception("Please select the genre for the RECORD or DVD.");
                }
            } else {
                //add this to avoid null Exception, because the genre is not selected yet
                //do not worry genre in GAME will not be recorded
                genreComboBox.getSelectionModel().selectFirst();
            }
        }
    }
    public void submitNewItem(ActionEvent event) throws Exception {
        System.out.println("Submit add item has been pressed");
        try {
            checkInput();

            itemIdNumber = this.rentalSystem.getHighestItemId();
            itemIdNumber++;

            String year = inputYearPublished.getText();
            String id = String.format("I%03d-%s", itemIdNumber, year);
            String title = inputTitle.getText();
            Enum.RentalType rentalType = Enum.RentalType.valueOf(rentalTypeComboBox.getValue());
            Enum.Genre genre = Enum.Genre.valueOf(genreComboBox.getValue());
            Enum.LoanType loanType = Enum.LoanType.valueOf(loanTypeComboBox.getValue());
            int numCopies = Integer.parseInt(inputNumCopies.getText());
            double fee = Double.parseDouble(inputFee.getText());
            boolean rentalStatus = false;
            if (numCopies > 0) {
                rentalStatus = true;
            } else {
                rentalStatus = false;
            }

            Item newItem = singleton.rentalSystem.addItem(id, title, rentalType, loanType, numCopies, rentalStatus, fee, genre);
            if(newItem == null){
                throw new Exception("An item has NOT been added successfully");
            }
            if(!singleton.rentalSystem.writeItemToFile()){
                throw new Exception("An item has NOT been saved to file successfully");
            } else {
                singleton.itemObservableList.add(newItem);
                singleton.SuccessModalDialog("SuccessAction.fxml", "You have successfully update an item in the list");
            }

        } catch (Exception error) {
            singleton.FailModalDialog("FailAction.fxml", error.getMessage());
        }
    }

    public void submitUpdateItem(ActionEvent event) {
        System.out.println("Submit update item has been pressed");
        try{
            checkInput();

            String year = inputYearPublished.getText();
            String id = String.format("I%03d-%s", itemIdNumber, year);
            String title = inputTitle.getText();
            Enum.RentalType rentalType = Enum.RentalType.valueOf(rentalTypeComboBox.getValue());
            Enum.Genre genre = Enum.Genre.valueOf(genreComboBox.getValue());
            Enum.LoanType loanType = Enum.LoanType.valueOf(loanTypeComboBox.getValue());
            int numCopies = Integer.parseInt(inputNumCopies.getText());
            double fee = Double.parseDouble(inputFee.getText());
            boolean rentalStatus = false;
            if (numCopies > 0) {
                rentalStatus = true;
            } else {
                rentalStatus = false;
            }

            Item updateItem = singleton.rentalSystem.updateItem(itemIdNumber, id, title, rentalType, loanType, numCopies, rentalStatus, fee, genre);
            if(updateItem == null){
                throw new Exception("An item has NOT been updated successfully");
            }
            if(!singleton.rentalSystem.writeItemToFile()){
                throw new Exception("An item has NOT been saved to file successfully");
            } else {
                singleton.itemObservableList.set(index, updateItem);
                singleton.SuccessModalDialog("SuccessAction.fxml", "You have successfully update an item in the list");
            }
        } catch (Exception error){
            try {
                singleton.FailModalDialog("FailAction.fxml", error.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

    }
}
