package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

import java.io.IOException;

public class Manage_ReservationController {

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_modify;

    @FXML
    private Button btn_remove;

    @FXML
    private ListView<?> list_view;

    // Instance of SceneController to switch scenes
    private SceneController sceneController = new SceneController();

    @FXML
    private void handleModifyButton(ActionEvent event) throws IOException {
        sceneController.switchToScene2(event);

    }
    @FXML
    private void goBack() throws Exception{
        SceneController.launchScene("Admin_ReservationTab.fxml");
    }
    @FXML
    private void goBackc() throws Exception{
        SceneController.launchScene("Customer_View.fxml");
    }
}
