package com.example.cinema_reservation_system;//Java program to set up connection and get all data from table
import java.sql.*;

public class GFG {
    public static void main(String arg[])
    {
        Connection connection = null;
        try {
            // below two lines are used for connectivity.
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test2",
                    "root", "");

            Statement statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(
                    "select * from cell");
            int cellid;
            String type;
            while (resultSet.next()) {
                cellid = resultSet.getInt("cell_id");
                type = resultSet.getString("type").trim();
                System.out.println("CellID : " + cellid
                        + " Type : " + type);
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
        }
    } // function ends
} // class ends
