package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class register_controller {

    @FXML
    private Button btn_btl;

    @FXML
    private Button btn_reg;

    @FXML
    private TextField name_field;

    @FXML
    private TextField uname_field;

    @FXML
    private TextField email_field;

    @FXML
    private TextField pass_field;

    @FXML
    private TextField cpass_field;

    @FXML
    private Label registerlabel;

    // Handle Back to Login
    @FXML
    private void backToLogin(ActionEvent event) throws IOException {
        SceneController.launchScene("login_page.fxml");
    }

    // Handle Registration Logic
    @FXML
    private void handleRegister(ActionEvent event) {
        String name = name_field.getText();
        String username = uname_field.getText();
        String email = email_field.getText();
        String password = pass_field.getText();
        String confirmPassword = cpass_field.getText();

        // Basic Validation
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerlabel.setText("Please fill in all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            registerlabel.setText("Passwords do not match!");
            return;
        }

        // Insert User Data
        try {
            Database db = Database.getInstance();
            String query = String.format(
                    "INSERT INTO accounts (name, username, email, password, isAdmin) VALUES('%s', '%s', '%s', '%s', 0)",
                    name, username, email, password
            );

            // Use executeUpdate instead of executeQuery for INSERT statement
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            int rowsAffected = stmt.executeUpdate(); // Executes the INSERT statement

            if (rowsAffected > 0) {
                registerlabel.setText("Registration Successful!");
                clearFields();  // Clear fields after successful registration
            } else {
                registerlabel.setText("Registration failed, please try again.");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            registerlabel.setText("Error: " + exception.getMessage());
        }
    }

    // Clear Fields After Successful Registration
    private void clearFields() {
        name_field.clear();
        uname_field.clear();
        email_field.clear();
        pass_field.clear();
        cpass_field.clear();
    }
}
