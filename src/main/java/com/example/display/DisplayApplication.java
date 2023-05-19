package com.example.display;

import javafx.application.Application;

import javafx.stage.Stage;

public class DisplayApplication extends Application {


    public static void main(String[] args) {
        Application.launch(args);
    }
    Singleton singleton =  Singleton.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        singleton.modalDialog("LandingPage.fxml");
    }
}
