package Backend;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Reusable DB connection utility class
 * Author: Boss 
 */
public class DBConnection {

    // Method to establish and return MySQL database connection
    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to 'fasttrack' database
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/fasttrack", // Your DB name
                    "root",                                   // Username
                    ""                                        // Password (blank by default)
            );
            System.out.println("✅ Connected to database.");
            return con;

        } catch (Exception e) {
            System.out.println("❌ Failed to connect to database.");
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        Connection con = getConnection();
        if (con != null) {
            System.out.println("✅ Connection Successful!");
        } else {
            System.out.println("❌ Connection Failed.");
        }
        
        }
    
}

