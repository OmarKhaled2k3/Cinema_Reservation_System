package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class Cus_Controller {

    @FXML
    private Label user_label;

    @FXML
    private Button btn_newreserv;

    @FXML
    private Button btn_mngreserv;

    @FXML
    private Button btn_logout;

    // Method to handle "New Reservation" button action
    @FXML
    private void handleNewReservation (ActionEvent event) throws Exception {
        Reservation reservation = Reservation.getInstance();
        reservation.getCustomer().setModify(false);
        SceneController.launchScene("SelectMovie.fxml");
    }

    // Method to handle "Manage Reservations" button action
    @FXML
    private void handleManageReservations(ActionEvent event) throws Exception {
        Reservation reservation = Reservation.getInstance();
        reservation.getCustomer().setModify(true);
        SceneController.launchScene("Customer_ManageReservationTab.fxml");
    }

    // Method to handle "Log Out" button action
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        SceneController.launchScene("login_page.fxml");
        // Add logic for logging out, such as navigating to the login screen
    }

    // You can initialize the controller here
    @FXML
    public void initialize() {

        user_label.setText("Guest");
    }
}
