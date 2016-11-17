import java.sql.*;

public class RubixCube {

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;

    public final static String TABLE_NAME = "rubix";
    public final static String PK_COLUMN = "ID";
    public final static String SOLVER_COLUMN = "Solver";
    public final static String SECONDS_COLUMN = "Seconds";

    public final static int MIN_TIME = 0;
    public final static int MAX_TIME = 99;

    private static RubixDataModel rubixDataModel;

    public static void main(String[] args) {
        // If setup or loading the table don't work, exit the program because all hope is lost
        if (!setup()) {
            System.exit(1);
        }
        if (!loadAllTimes()) {
            System.exit(1);
        }
        // creating GUI
        RubixCubeGUI rubixGUI = new RubixCubeGUI(rubixDataModel);
    }

    // Attempts to fill table with data from existing table
    public static boolean loadAllTimes(){
        try{
            if (rs!=null) {
                rs.close();
            }
            String getAllData = "SELECT * FROM " + TABLE_NAME;
            rs = statement.executeQuery(getAllData);

            if (rubixDataModel == null) {
                rubixDataModel = new RubixDataModel(rs);
            } else {
                rubixDataModel.updateResultSet(rs);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error loading or reloading table. Printing stacktrace and quitting program...");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setup(){
        try {
            DBUtils.getDriver();
            conn = DBUtils.getConnection();
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // If the rubix table does not exist, create it and add sample data
            if (!rubixTableExists()) {
                String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + SOLVER_COLUMN + " varchar(50), " + SECONDS_COLUMN + " double, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                String addDataSQL = "INSERT INTO " + TABLE_NAME + "(" + SOLVER_COLUMN + ", " + SECONDS_COLUMN + ") " + " VALUES ('Cubestormer II robot', 5.27)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + TABLE_NAME +  "(" + SOLVER_COLUMN + ", " + SECONDS_COLUMN + ") " + " VALUES('Fakhri Raihaan', 27.93)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + TABLE_NAME +  "(" + SOLVER_COLUMN + ", " + SECONDS_COLUMN + ") " + " VALUES ('Ruxin Liu', 99.33)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + TABLE_NAME +  "(" + SOLVER_COLUMN + ", " + SECONDS_COLUMN + ") " + " VALUES ('Mats Valk', 6.27)";
                statement.executeUpdate(addDataSQL);
            }
            return true;
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    // Finding whether or not a table with name as above exists
    private static boolean rubixTableExists() throws SQLException {
        String checkTablePresentQuery = "SHOW TABLES LIKE '" + TABLE_NAME + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {
            return true;
        }
        return false;
    }

    //Closing ResultSet, statement and connection on shutdown
    public static void shutdown(){
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}