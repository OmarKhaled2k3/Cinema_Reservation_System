package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MovieController implements Initializable {
    Database db = Database.getInstance();
    @FXML
    HBox hb = new HBox();
    @FXML
    private Button backButton;

    @FXML
    private GridPane grid;
    @FXML
    ImageView pic;
    @FXML
    Image image;
    @FXML
    private ScrollPane scrollPane;

    public void initialize(URL location, ResourceBundle resources) {
        // gridpane settings
        // setting exterior grid padding
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        grid.setPadding(new Insets(7,7,7,7));
        // setting interior grid padding
        grid.setHgap(10);
        grid.setVgap(10);
        // grid.setGridLinesVisible(true);
        ArrayList<Movie>movies=retrieveMovies();
        int rows = (movies.size() / 4) + 1;
        int columns = 4;
        int movieID = 0;


        for (int i = 0 ; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (movieID < movies.size()) {
                    int id =movies.get(movieID).getId();
                    String url =movies.get(movieID).getImageurl();
                    addImage(id,url,j,i);
                    movieID++;
                }
            }
        }

    }

    private void addImage(int id,String imageurl, int colIndex, int rowIndex) {
        image = new Image(imageurl);
        pic = new ImageView();
        pic.setFitWidth(160);
        pic.setFitHeight(220);
        pic.setImage(image);
        pic.setId(String.valueOf(id));
        hb.getChildren().add(pic);
        GridPane.setConstraints(pic, colIndex, rowIndex, 1, 1, HPos.CENTER, VPos.CENTER);
        // grid.add(pic, imageCol, imageRow);
        grid.getChildren().addAll(pic);

    }

    private ArrayList<Movie> retrieveMovies(){
        ArrayList<Movie>movies=new ArrayList<Movie>();
        try {
            Database db = Database.getInstance();
            ResultSet resultSet = db.executeQuery("select * from movies");

            if (resultSet != null) {
                while (resultSet.next()) {
                    int movieId = resultSet.getInt("id");
                    String genre=resultSet.getString("genre").trim();
                    String title=resultSet.getString("title").trim();
                    //int duration= Integer.parseInt(resultSet.getString("duration").trim().split("")[0]);
                    int duration= 10;
                    String description=resultSet.getString("description").trim();
                    String imageurl=resultSet.getString("imageurl").trim();
                    Movie movie = new Movie(movieId,title,genre,duration,imageurl);
                    movies.add(movie);
                    //System.out.println("CellID : " + movieId + " Type : " + type);
                }
                resultSet.close();
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        return movies;
    }


}
