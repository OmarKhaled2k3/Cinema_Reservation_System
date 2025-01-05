package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private void goBack() throws Exception{
        SceneController.launchScene("Admin_View.fxml");
    }
    @FXML
    private void gotomanage() throws Exception{
        SceneController.launchScene("Admin_ManageReservationTab.fxml");
    }

    @FXML
    private void gotomanage2() throws Exception{
        SceneController.launchScene("Admin_Showtimetab1.fxml");
    }

    @FXML
    private void goBack1() throws Exception{
        SceneController.launchScene("Admin_Showtimetab1.fxml");
    }

}
