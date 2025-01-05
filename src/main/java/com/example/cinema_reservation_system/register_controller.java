package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;  // Correct import
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class register_controller {

    @FXML
    private Button btn_btl;

    @FXML
    private Button btn_reg;

    @FXML
    private Label confirmpasswordlabel;

    @FXML
    private TextField cpass_field;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField email_field;

    @FXML
    private TextField name_field;

    @FXML
    private Label namelabel;

    @FXML
    private TextField pass_field;

    @FXML
    private Label passwoedlabel;

    @FXML
    private Label registerlabel;

    @FXML
    private TextField uname_field;

    @FXML
    private Label usernamelabel;

    // Corrected method to handle the back button action
    @FXML
    private void backToLogin(ActionEvent event) throws IOException {
        SceneController.launchScene("login_page.fxml");  // Use the SceneController to switch to the login scene
    }
}
