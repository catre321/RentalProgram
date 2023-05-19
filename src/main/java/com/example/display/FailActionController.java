package com.example.display;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FailActionController {
    @FXML
    private Label errorMessage;

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }
}