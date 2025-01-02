package com.example.cinema_reservation_system;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Customer extends User implements Reservable {
    public Customer(String name, String email, String username, String password) {
        super(name, email,username,password);

    }
    @Override
    public void login() {
        System.out.println(name + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println(name + " logged out.");
    }

    @Override
    public void createReservation() {
        // Example logic for creating a reservation
        System.out.println("Creating reservation for customer " + name);
    }
    //ShowTime showTime = new ShowTime(Movie,starttime);
    // Seats
    //ShowTime showTime = new ShowTime(new Movie("dsfsdf","sdfs",50), LocalDateTime.now());
    //Reservation reservation = new Reservation(new Customer("sdf","sdf","sdf","sdf") ,showTime,new ArrayList<Seat>());
    @Override
    public void cancelReservation() {
        // Example logic for canceling a reservation
        System.out.println("Canceling reservation for customer " + name);
    }
    public void createFoodOrder(FoodOrder foodOrder){
        System.out.println("making Food order for customer."+ name);
    }
    public void cancelFoodOrder(FoodOrder foodOrder){
        System.out.println("canceling Food order for customer."+ name);
    }
    public void viewReservation() {
        // Example logic for viewing a reservation
        System.out.println("Viewing reservation history for " + name);
    }

}
