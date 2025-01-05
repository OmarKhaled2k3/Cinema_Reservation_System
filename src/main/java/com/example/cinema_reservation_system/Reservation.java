package com.example.cinema_reservation_system;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Reservation implements Printable {

    // Singleton instance
    private static Reservation instance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Variables
    private int id;
    private Customer customer;            // Customer object
    private int movieId;                  // Selected movie ID
    private ShowTime showtime;            // Showtime object
    private FoodOrder foodOrder;       // Food items list
    private ArrayList<Seat> seats;    // Selected seats list

    // Private constructor
    private Reservation() {
        reset(1);
    }

    // Singleton instance getter
    public static synchronized Reservation getInstance() {
        if (instance == null) {
            instance = new Reservation();
        }
        return instance;
    }

    // Getter and Setter for Customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Getter and Setter for movieId
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    // Getter and Setter for movieName (optional)
    public Movie getMoviebyID(int id){
        Movie movie=null;
        try {
            Database db = Database.getInstance();
            ResultSet resultSet = db.executeQuery("select * from movies where id=" + String.valueOf(id) );
            if (resultSet != null) {
                while (resultSet.next()) {
                    int movieId = resultSet.getInt("id");
                    String genre=resultSet.getString("genre").trim();
                    String title=resultSet.getString("title").trim();
                    int duration= Integer.parseInt(resultSet.getString("duration").trim().split("")[0]);
                    //int duration= 10;
                    String description=resultSet.getString("description").trim();
                    String imageurl=resultSet.getString("imageurl").trim();
                    movie = new Movie(movieId,title,genre,duration,description,imageurl);
                }
                resultSet.close();
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        return movie;
    }

    // Getter and Setter for Showtime
    public ShowTime getShowtime() {
        return showtime;
    }

    public void setShowtime(ShowTime showtime) {
        this.showtime = showtime;
    }

    // Methods to manage foodOrder
    public FoodOrder getFoodOrder() {return foodOrder;}
    public void addFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }
    public void addFoodOrder(ArrayList<FoodItem> foodItems) {
        FoodOrder foodOrder1 = new FoodOrder();
        foodOrder1.addItems(foodItems);
        this.foodOrder = foodOrder1;
    }
    public void addFoodOrder(FoodItem foodItem) {
        FoodOrder foodOrder1 = new FoodOrder();
        foodOrder1.addItems(foodItem);
        this.foodOrder = foodOrder1;
    }
    public void clearFoodOrder() {
        this.foodOrder = null;
    }

    // Methods to manage seatSelected
    public ArrayList<Seat> getSeatSelected() {
        return seats; // Return a copy to prevent modification
    }
    public String getSeatSelectedString() {
        return Seat.SeatsConversiontoString(seats);
    }

    public void addSeatSelected(Seat seat) {
        this.seats.add(seat);
    }
    public void addSeatSelected(ArrayList<Seat> seats) {
        this.seats = seats;
    }
    public void addSeatSelected(String seatString) {
        this.seats = Seat.SeatsConversion(seatString);
    }

    public void clearSeatSelected() {
        this.seats.clear();
    }

    // Reset all fields
    public void reset(int id) {
        this.customer = null;
        this.movieId = 0;
        this.showtime = null;
        this.foodOrder=null;
        this.seats=null;
        this.id=id;
    }
    public void printDetails() {
        System.out.println("Reservation ID: " + id);
        customer.printDetails();
        showtime.printDetails();
        for(Seat seat:seats){
            seat.printDetails();
        }

    }
}
