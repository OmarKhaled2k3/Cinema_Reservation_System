package com.example.cinema_reservation_system;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ShowTime implements Printable {
    private Movie movie;


    private LocalDateTime startTime;
    private ArrayList<Seat> seatList;
    private int id;

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getStartTime() { return startTime; }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ShowTime(Movie movie, LocalDateTime startTime) {
        this.movie = movie;
        this.startTime = startTime;
        seatList = new ArrayList<>(80);
    }
    public void reserveShowTimeSeats(ArrayList<Seat>seats){
       for(Seat seat : seats){
           seatList.get(seat.getSeatNumber()).setReserved(true);
        }
    }
    public void cancelShowTimeSeats(ArrayList<Seat>seats){
        for(Seat seat : seats){
            seatList.get(seat.getSeatNumber()).setReserved(false);
        }
    }
    public Movie getMovie() { return movie; }

    public int numberofSeatsAvailable(){
        int available=0;
        for(Seat seat : seatList){
            if(seat.isReserved())
                available++;
        }
        return available;
    }

    public void printDetails () {
        System.out.println( "ShowTime{" +
                "movie=" + movie +
                ", startTime=" + startTime +
                ", numberofSeatsAvailable=" + numberofSeatsAvailable() +
                '}');
    }
}
