import java.util.Scanner;

public class Admin extends User implements Reservable{

    public Admin(String name, String email, String username, String password) {
        super(name, email,username,password);

    }
    @Override
    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();

        if (authenticate(inputUsername, inputPassword)) {
            System.out.println(name + " (Admin) logged in.");
        } else {
            System.out.println("Invalid credentials. Login failed.");
        }
    }
    // Authenticate method
    private boolean authenticate(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.getPassword().equals(inputPassword);
    }
    @Override
    public void logout() {
        System.out.println(name + " (Admin) logged out.");
    }

    @Override
    public void createReservation() {
        // Admin can create reservations, maybe for other customers
        System.out.println("Admin " + name + " creating a reservation.");
    }

    @Override
    public void cancelReservation() {
        // Admin can cancel reservations
        System.out.println("Admin " + name + " canceling a reservation.");
    }
    public void listAllReservations() {
        // Admin can cancel reservations
        System.out.println("Admin " + name + " viewing all reservation history.");
    }
    public void AddShowTime(ShowTime showTime){
        // Admin can add show time
        System.out.println("Admin " + name + " adding showtime.");
    }
    public void DeleteShowTime(ShowTime showTime){
        System.out.println("Admin " + name + " deleting showtime.");
    }
    public void AddMovie(Movie movie){
        System.out.println("Admin " + name + " adding Movie.");
    }
    public void DeleteMovie(Movie movie){
        System.out.println("Admin " + name + " deleting Movie.");
    }
    public void AddFoodItem(FoodItem foodItem){
        System.out.println("Admin " + name + " adding Food item to the menu.");
    }
    public void DeleteFoodItem(FoodItem foodItem){
        System.out.println("Admin " + name + " deleting Food item to the menu.");
    }
    public void CreateFoodOrderForCustomer(FoodOrder foodOrder,Customer customer){
        System.out.println("Admin " + name + " making Food order for customer.");
    }
    public void CancelFoodOrderForCustomer(FoodOrder foodOrder,Customer customer){
        System.out.println("Admin " + name + " canceling Food order for customer.");
    }
    public void MakeReservationForCustomer(Reservation reservation,Customer customer){
        System.out.println("Admin " + name + " making reservation  for customer.");
    }
    public void CancelReservationForCustomer(Reservation reservation,Customer customer){
        System.out.println("Admin " + name + " canceling reservation for customer.");
    }
    public void ListAllServices(){
        System.out.println("Admin " + name + " viewing Movies,Showtimes,fooditems.");
    }

}
