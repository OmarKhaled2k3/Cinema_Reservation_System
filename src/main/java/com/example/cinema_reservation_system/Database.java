package com.example.cinema_reservation_system;

import java.sql.*;

public class Database {
    // Singleton instance
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            // Initialize the connection
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cinema",
                    "root", ""
            );
        } catch (SQLException exception) {
            System.out.println("Database connection error: " + exception);
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException exception) {
            System.out.println("Query execution error: " + exception);
            return null;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            Database db = Database.getInstance();
            ResultSet resultSet = db.executeQuery("SELECT * FROM movies");

            if (resultSet != null) {
                while (resultSet.next()) {
                    int cellId = resultSet.getInt("ID");
                    String type = resultSet.getString("title").trim();
                    System.out.println("CellID : " + cellId + " Type : " + type);
                }
                resultSet.close();
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
}
