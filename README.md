# Cinema Reservation System

## Overview
The Cinema Reservation System is a Java-based application that provides an interactive and user-friendly platform for managing cinema reservations. Users can log in, select seats, make reservations, and order food. Admins have additional privileges, including managing movies, showtimes, reservations, and cafeteria operations.

## Features

### User Features
- **User Accounts**: Users can log in using their credentials stored in the `accounts` table in the database.
- **Make Reservations**: Select movie, showtime, and seats to create a reservation.
- **Delete Reservations**: Cancel existing reservations.
- **Manage Reservations**: View and modify reservations.
- **Order Food**: Choose food items and quantities during the reservation process.

### Admin Features
- **Admin Login**: Admin accounts allow access to advanced features.
- **Add Movies**: Add new movies to the database.
- **Add Showtimes**: Schedule showtimes for movies.
- **Manage Reservations**: Oversee and modify user reservations.
- **Manage Cafeteria**: Update food menus and pricing.



## Java Classes Overview

### Core Classes
1. **`Reservation` (Singleton Pattern)**  
   - Tracks the reservation process, including:
     - Selected movie, showtime, and seats.
     - Food orders.
   - Provides methods for managing reservation data.

2. **`Seat`**
   - Represents an individual seat.
   - Tracks attributes like seat number, type (`Standard`, `VIP`), and reservation status.
   - Provides utilities for seat conversions (e.g., to/from strings).

3. **`FoodOrder`**
   - Represents a user's food order.
   - Tracks items, quantities, and provides utility methods for serialization and deserialization.

4. **`Database` (Singleton Pattern)**  
   - Manages the database connection using JDBC.
   - Provides methods for executing queries and updates.
   - Abstracts database interactions for other components.

5. **`Customer` and `Showtime`**  
   - **Customer**: Represents users interacting with the system, encapsulating user details.  
   - **Showtime**: Encapsulates the movie timing and seat availability.  

6. **`Movie`**
   - Represents a movie entity with attributes like name, genre, and duration.

### JavaFX-Specific Components
- **Login Screen**: Authenticates users and admins.
- **Movie Selection Screen**: Displays available movies.
- **Seat Selection Screen**: Allows users to select seats and highlights unavailable ones.
- **Cart Screen**: Summarizes reservations, including seat and food selections.



## Database Overview
While the system relies on a MySQL database for persistence, the application focuses on Java classes to manage business logic. The database includes:
- **Accounts** for authentication.
- **Movies** and **Showtimes** for scheduling.
- **Reservations** and **FoodOrder** for user actions.



## Setup Instructions

1. Clone the repository:

   ```bash
   git clone https://github.com/OmarKhaled2k3/Cinema_Reservation_System.git
   ```

2. Configure the MySQL database:
   - Import the provided SQL script to create necessary tables and seed initial data.
   - Update database connection details in the `Database` class.

3. Run the application:
   ```bash
   mvn javafx:run
   ```



## Usage

1. **Login**: Use your credentials to log in as a user or admin.
2. **User Actions**:
   - Select a movie and showtime.
   - Choose seats and confirm the reservation.
   - Optionally, add food items to your order.
   - View, modify, or cancel your reservations.
3. **Admin Actions**:
   - Add or modify movies and showtimes.
   - Manage user reservations.
   - Update cafeteria items and prices.



## Technologies Used
- **JavaFX**: For the user interface.
- **Scene Builder**: For designing the JavaFX UI.
- **MySQL**: For the database.
- **JDBC**: To connect and interact with the database.
- **Maven**: For build automation.
- **IntelliJ IDEA**: As the development environment.

---

## Screenshots
<img src="https://github.com/user-attachments/assets/4f90afcc-c577-4ca0-8cab-b4748304ce34" width="400">
<img src="https://github.com/user-attachments/assets/6c7c847c-62d7-4672-b2b6-12dba3d38dc8" width="400">
<img src="https://github.com/user-attachments/assets/b2d252d6-06fc-4419-85ec-dffd226e4736" width="400">
<img src="https://github.com/user-attachments/assets/09cc9f14-f37d-400e-aab0-34f85d74b418" width="400">
<img src="https://github.com/user-attachments/assets/dc3bedc6-400a-4a65-b360-ed9fc9587dfd" width="400">
<img src="https://github.com/user-attachments/assets/a574fd90-e528-448a-9ae2-5b733467d366" width="400">

---

## Contributing
Contributions are welcome! Please fork the repository and create a pull request for any improvements or bug fixes.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---
Feel free to reach out for any issues or feature requests!

