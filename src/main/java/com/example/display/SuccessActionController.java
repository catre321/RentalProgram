package com.example.display;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Label;

import javafx.stage.Stage;

public class SuccessActionController {
    @FXML
    private Label message;

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setMessage(String msg) {
        message.setText(msg);
    }
}