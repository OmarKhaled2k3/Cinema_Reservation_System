import java.util.ArrayList;

public class Reservation implements Printable {
    private int id;
    private ShowTime showTime;
    private Customer customer;
    private ArrayList<Seat>seats;
    public Reservation(Customer customer, ShowTime showTime, ArrayList<Seat>seats) {
        showTime.reserveShowTimeSeats(seats);
        this.showTime = showTime;
        this.customer = customer;
        this.seats = seats;

    }
    public void printDetails() {
        System.out.println("Reservation ID: " + id);
         customer.printDetails();
         showTime.printDetails();
         for(Seat seat:seats){
             seat.printDetails();
         }

    }
}
