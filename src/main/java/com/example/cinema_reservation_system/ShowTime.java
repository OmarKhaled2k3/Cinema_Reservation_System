import java.time.LocalDateTime;
import java.util.ArrayList;

public class ShowTime implements Printable {
    private Movie movie;
    private LocalDateTime startTime;
    private ArrayList<Seat> seatList;
    public ShowTime(Movie movie, LocalDateTime startTime) {
        this.movie = movie;
        this.startTime = startTime;
        seatList = new ArrayList<>(70);
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
    public LocalDateTime getStartTime() { return startTime; }
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
