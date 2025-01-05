package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            // Login success: error message is hidden and the user is redirected appropriately
            wrongCredentials.setVisible(false);
        } else {
            // Show error message for invalid login
            wrongCredentials.setText("Invalid username or password. Please try again.");
            wrongCredentials.setVisible(true);
        }
    }

    @FXML
    private void launchRegister(ActionEvent event) throws IOException {
        SceneController.launchScene("Register_page.fxml"); // Open the registration page
    }

    private boolean isValidLogin(String username, String password) {
        Database db = Database.getInstance();
        String query = "SELECT name, isAdmin FROM accounts WHERE username = ? AND password = ?";

        try (PreparedStatement preparedStatement = db.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean isAdmin = resultSet.getInt("isAdmin") == 1;

                // Redirect based on isAdmin value and pass first name to AdminPageController
                if (isAdmin) {
                    AdminPageController.setName(name);  // Assuming you add a setter in AdminPageController
                    SceneController.launchScene("Admin_View.fxml");
                } else {
                    SceneController.launchScene("Customer_View.fxml");
                }
                return true; // Login is valid
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false; // Login is invalid
    }

}
