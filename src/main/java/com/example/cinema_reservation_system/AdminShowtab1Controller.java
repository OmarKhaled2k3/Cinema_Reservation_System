package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminShowtab1Controller implements Initializable {

    @FXML
    private ComboBox<String> box_movies;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_continue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the ComboBox with movies from the database
        box_movies.setItems(FXCollections.observableArrayList(fetchMovies()));

        // Disable continue button initially
        btn_continue.setDisable(true);

        // Enable continue button when a movie is selected
        box_movies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btn_continue.setDisable(newValue == null);
        });
    }

    // Fetch movie names from the database
    private ArrayList<String> fetchMovies() {
        ArrayList<String> movies = new ArrayList<>();
        try {
            Database db = Database.getInstance();
            String query = "SELECT title FROM movies";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(rs.getString("title"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @FXML
    private void goBack() throws IOException {
        SceneController.launchScene("Admin_View.fxml");
    }

    @FXML
    private void gotomanage2() throws IOException {
        SceneController.launchScene("Admin_Showtimetab2.fxml");
    }
}
