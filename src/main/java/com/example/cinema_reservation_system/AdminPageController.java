package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {

    @FXML
    private Button manageMovieButton;
    @FXML
    private Button manageBookingsButton;
    @FXML
    private Button manageFoodButton;
    @FXML
    private Button manageShowtimesButton;
    @FXML
    private Button logOutButton;

    @FXML
    private Label windowTitleLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label firstNameLabel;

    // Correct initialization method, annotated with @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Enable buttons on initialization
        manageMovieButton.setDisable(false);
        manageBookingsButton.setDisable(false);
        manageFoodButton.setDisable(false);
        manageShowtimesButton.setDisable(false);
        logOutButton.setDisable(false);
    }

    // This method is triggered when the "Manage Movies" button is clicked
    @FXML
    private void launchMovie(ActionEvent event) throws IOException {
        SceneController.launchScene("Movie.fxml");
    }

    // This method is triggered when the "Manage Bookings" button is clicked
    @FXML
    private void launchReservations(ActionEvent event) throws IOException {
        SceneController.launchScene("Admin_ReservationTab.fxml");
    }

    // This method is triggered when the "Manage Cafeteria" button is clicked
    @FXML
    private void launchFood(ActionEvent event) throws IOException {
        SceneController.launchScene("Food_Admin.fxml");
    }

    // This method is triggered when the "Manage Showtimes" button is clicked
    @FXML
    private void launchShowtimes(ActionEvent event) throws IOException {
        SceneController.launchScene("Admin_Showtimetab1.fxml");
    }

    // This method is triggered when the "Log Out" button is clicked
    @FXML
    private void logOut(ActionEvent event) throws IOException {
        SceneController.launchScene("login_page.fxml");  // Assuming 'login_page' is an FXML file, use the correct name
    }
}
