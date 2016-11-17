import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/rubix";
    private static final String USER = "alyssa";
    private static final String PASSWORD = "kittens";

    static void getDriver() {
        try {
            String driver = JDBC_DRIVER;
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No database drivers found. Please ensure your " +
                    "existing dependencies are correct.\nExiting program...");
            System.exit(1);
        }
    }

    static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
        } catch (SQLException sqle) {
            System.out.println("Unable to connect to database. Please make sure that MySQL is Running, the" +
                    " rubix database exists with correct permissions, and the username/password is correct.");
            System.out.println("Printing stack trace and exiting program...");
            sqle.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}