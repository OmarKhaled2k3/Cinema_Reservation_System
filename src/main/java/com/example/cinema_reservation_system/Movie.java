package com.example.cinema_reservation_system;

public class Movie implements Printable  {
    private String title;
    private String genre;
    private int duration; // in minutes
    private int id;
    private String imageurl;

    public Movie(int id,String title, String genre, int duration, String imageurl) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.id = id;
        this.imageurl = imageurl;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }

    public int getId() {
        return id;
    }

    public String getImageurl() {
        return imageurl;
    }

    @Override
    public void printDetails() {
        System.out.println("Movie ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Genre: " + genre);
        System.out.println("Duration: " + duration + " minutes");
    }
}
