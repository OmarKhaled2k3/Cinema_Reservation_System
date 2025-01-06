package com.example.cinema_reservation_system;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Movie implements Printable {
    private StringProperty title;
    private StringProperty genre;
    private IntegerProperty duration; // in minutes
    private IntegerProperty id;
    private StringProperty imageurl;
    private StringProperty description;

    public Movie(int id, String title, String genre, int duration, String description, String imageurl) {
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.duration = new SimpleIntegerProperty(duration);
        this.id = new SimpleIntegerProperty(id);
        this.imageurl = new SimpleStringProperty(imageurl);
        this.description = new SimpleStringProperty(description);
    }

    @Override
    public String toString() {
        return title.get();
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty getGenreProperty() {
        return genre;
    }

    public int getDuration() {
        return duration.get();
    }

    public IntegerProperty getDurationProperty() {
        return duration;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public String getImageurl() {
        return imageurl.get();
    }

    public StringProperty getImageurlProperty() {
        return imageurl;
    }

    @Override
    public void printDetails() {
        System.out.println("Movie ID: " + id.get());
        System.out.println("Title: " + title.get());
        System.out.println("Genre: " + genre.get());
        System.out.println("Duration: " + duration.get() + " minutes");
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setImageurl(String imageurl) {
        this.imageurl.set(imageurl);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
