package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMovieController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<?> table_view;

    @FXML
    private TableColumn<?, ?> name_column;

    @FXML
    private TableColumn<?, ?> genre_column;

    @FXML
    private TableColumn<?, ?> duration_column;

    @FXML
    private TableColumn<?, ?> url_column;

    @FXML
    private TableColumn<?, ?> desc_column;

    @FXML
    private HBox buttonBox;

    @FXML
    private Button back_button;

    @FXML
    private Button add_button;

    @FXML
    private Button remove_button;

    @FXML
    private Button save_button;

    @FXML
    private StackPane titlePane;

    @FXML
    private Label movie_title;

    @FXML
    private VBox leftVBox;

    @FXML
    private Label name;

    @FXML
    private Label genre1;

    @FXML
    private Label duration1;

    @FXML
    private Label imageUrlLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private VBox rightVBox;

    @FXML
    private TextField namebox;

    @FXML
    private TextField genrebox;

    @FXML
    private TextField durationbox;

    @FXML
    private TextField imageurlbox;

    @FXML
    private TextArea textarea;

    // This method will be called once the FXML file is loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the TableView with some sample data or structure
        setupTableView();

        // Set up button actions
        back_button.setDisable(false);
        add_button.setDisable(false);
        remove_button.setDisable(false);
        save_button.setDisable(false);
    }

    private void setupTableView() {
        // Here you can initialize the table columns with data or set cell factories if needed
        // Example: name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void goBack() throws Exception{
    SceneController.launchScene("Admin_View.fxml");
    }

    @FXML
    private void addMovie() {

    }
  @FXML
    private void removeMovie() {

    }
  @FXML
    private void saveMovie() {

    }
}
