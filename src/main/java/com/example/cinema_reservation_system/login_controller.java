package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class login_controller implements Initializable {

    @FXML
    private TextField usernameBox;
    @FXML
    private PasswordField passwordBox;
    @FXML
    private Button logInButton;
    @FXML
    private Button registerbutton;
    @FXML
    private Label wrongCredentials;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ensure buttons are active
        logInButton.setDisable(false);
        registerbutton.setDisable(false);
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameBox.getText();
        String password = passwordBox.getText();

        if (isValidLogin(username, password)) {
            // Hide the error message if login is successful
            wrongCredentials.setVisible(false);
            SceneController.launchScene("Admin_View.fxml");  // Redirect to Admin Page after successful login
        } else {
            // Show the error message if login fails
            wrongCredentials.setText("Invalid username or password. Please try again.");
            wrongCredentials.setVisible(true);  // Make the label visible
        }
    }

    @FXML
    private void launchRegister(ActionEvent event) throws IOException {
        SceneController.launchScene("Register_page.fxml");  // Open the registration page
    }

    private boolean isValidLogin(String username, String password) {
        // Dummy login logic, replace with real validation
        return username.equals("admin") && password.equals("password");
    }
}
