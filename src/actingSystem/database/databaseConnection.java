package actingSystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseConnection {

    private static final String databaseName="accountingsystem";

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/accountingsystem";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public static Connection getConnectionWithoutDb() throws SQLException {
        String url = "jdbc:mysql://localhost:3306";
        String user = "root";
        String password = "";
        checkDatabase();
        return DriverManager.getConnection(url, user, password);
    }

    private static void checkDatabase() {
        String sql = "SHOW DATABASES";
        boolean databaseExist = false;
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql)) {

            while (result.next()) {
                String dbeNames = result.getString(1);
                if(dbeNames.equals(databaseName)) {
                    databaseExist =true;
                    break;
                }
            }
 
            if(!databaseExist) {
                sql =  "CREATE DATABASE " + databaseName;
                stmt.executeUpdate(sql);
                checkTable(conn,stmt);
            } else {
                checkTable(conn,stmt);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();  // Or handle the exception as needed
        }
    }

    private static void checkTable(Connection conn, Statement stmt) throws SQLException {
        String sql;
        try {
            sql="USE "+databaseName;
            stmt.execute(sql);
            sql="CREATE TABLE IF NOT EXISTS accountant(\n" + 
                "acct_id int primary key auto_increment,\n" + 
                "acct_userName varchar(50) not null,\n" + 
                "acct_password varchar(30) not null\n" + 
                ");";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS dataCollection (\n" +
                     "    counter INT PRIMARY KEY AUTO_INCREMENT,\n" +
                     "    entDate DATE NOT NULL,\n" +
                     "    debit VARCHAR(30) NOT NULL,\n" +
                     "    credit VARCHAR(30) NOT NULL,\n" +
                     "    descp VARCHAR(40) NOT NULL,\n" +
                     "    amount DOUBLE NOT NULL,\n" +
                     "    acct_id int,\n"+
                     "    foreign key(acct_id) references accountant(acct_id)\n"+
                     ");";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS viewer(\n" + //
                    "user_id int primary key auto_increment,\n" + //
                    "user_userName varchar(50) not null,\n" + //
                    "user_password varchar(30) not null\n" + //
                    ");";
            stmt.executeUpdate(sql);      
        } catch (SQLException e) {
            e.printStackTrace();  // Or handle the exception as needed
        }
    }
    
}