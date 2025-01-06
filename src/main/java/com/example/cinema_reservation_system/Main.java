package com.example.cinema_reservation_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
    static Parent root;
    static Stage primaryStage;
    static Parent getRoot() {

        return root;
    }
    static void setRoot(Parent root) {

        Main.root = root;
    }

    static Stage getStage() {

        return primaryStage;
    }

    static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }
    @Override
    public void start(Stage primaryStage) {
        try {

            root = FXMLLoader.load(getClass().getResource("SelectMovie.fxml"));
            Main.primaryStage = primaryStage;
            primaryStage.setTitle("Cinema Reservation System");
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}