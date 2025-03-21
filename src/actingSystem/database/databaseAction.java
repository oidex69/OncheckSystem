package actingSystem.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Result;

import actingSystem.LinkedNode;

public class databaseAction {
    
    public int count(){
        String query ="SELECT COUNT(*) FROM datacollection";

        try(Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rstSet = statement.executeQuery(query)){
            if(rstSet.next()){
                int index = rstSet.getInt(1);
                return index;
            }
        }catch(Exception e){

        }
        return 0;
    }

    public int countWithDate(LocalDate date,LocalDate endDate){
        String query ="SELECT COUNT(*) FROM datacollection WHERE entDate between ? and ? AND acct_id = ?";
        try(Connection connection = databaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setDate(1, Date.valueOf(date));
            statement.setDate(2,Date.valueOf(endDate));
            statement.setInt(3, LinkedNode.getAcctId());
            statement.setInt(3, 1);
            try(ResultSet rstSet = statement.executeQuery()) {
                if(rstSet.next()) {
                    int index =rstSet.getInt(1);
                    return index;
                }
            }
        }catch(Exception e){

        }
        return 0;
    }

    public void save(int index,LinkedNode node){
        // String[] name = {"hello","world","1","2","#"}; //example for hellotry database

        String query = "INSERT INTO datacollection (entDate,debit,credit,descp,amount,acct_id) VALUES (?,?,?,?,?,?)";
        
        try (Connection connection = databaseConnection.getConnection(); // Call the method from databaseConnection class
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
                for (int i=0;i < index ;i++) {
                    // Ensure the arrays have values at this index
                        preparedStatement.setDate(1,Date.valueOf(node.getDate()));
                        preparedStatement.setString(2, node.debit[i]);
                        preparedStatement.setString(3, node.credit[i]);
                        preparedStatement.setString(4, node.descp[i]);
                        preparedStatement.setDouble(5, node.amount[i]);
                        preparedStatement.setInt(6, LinkedNode.getAcctId());
                        preparedStatement.addBatch();  // Add to batch
            }
            
            int[] rowsInserted = preparedStatement.executeBatch();
            if (rowsInserted.length > 0) {
                // System.out.println("A new user was inserted successfully!");
            }

        } catch (SQLException exception) {
            System.out.println("Error inserting user: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
    
    public LinkedNode read(int dataIndex,LocalDate date,LocalDate endDate) {
        int i = 0;
        LinkedNode node = new LinkedNode();
        node.initialize(dataIndex); // Initialize node arrays to hold dataCount items

        String sql = "SELECT * FROM datacollection WHERE entDate between ? and ? and acct_id = ?";

        try (Connection connection = databaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
        // Set the date parameter in the query
        statement.setDate(1, Date.valueOf(date));
        statement.setDate(2,Date.valueOf(endDate));
        statement.setInt(3, LinkedNode.getAcctId());

        try (ResultSet result = statement.executeQuery()) {
            // Use while loop to iterate over ResultSet rows
            while (result.next() && i < dataIndex) {
                node.addDebit(i, result.getString("debit"));
                node.addCredit(i, result.getString("credit"));
                node.adddesc(i, result.getString("descp"));
                node.addamount(i, result.getDouble("amount"));
                i++;
            }
            node.printAll(i);  // Print all retrieved data for debugging purposes
        }
    } catch (Exception e) {
        e.printStackTrace(); // Log exception details for debugging
    }
        return node;
    }

    public LinkedNode readForEdit(int dataIndex,LocalDate date,LocalDate endDate){
        int i = 0;
        LinkedNode node = new LinkedNode();
        node.initializeEdit(dataIndex); // Initialize node arrays to hold dataCount items
        String sql = "SELECT * FROM datacollection WHERE entDate between ? and ? and acct_id = ?";

        try (Connection connection = databaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
        
        // Set the date parameter in the query
        statement.setDate(1, Date.valueOf(date));
        statement.setDate(2,Date.valueOf(endDate));
        statement.setInt(3, LinkedNode.getAcctId());

        try (ResultSet result = statement.executeQuery()) {
            // Use while loop to iterate over ResultSet rows
            while (result.next() && i < dataIndex) {
                node.setDataCollectionCounter(i, result.getInt("counter"));
                node.setEditDate(i, result.getDate("entDate").toLocalDate());
                node.addDebit(i, result.getString("debit"));
                node.addCredit(i, result.getString("credit"));
                node.adddesc(i, result.getString("descp"));
                node.addamount(i, result.getDouble("amount"));
                i++;
            }
            node.printAll(i);  // Print all retrieved data for debugging purposes
        }
    } catch (Exception e) {
        e.printStackTrace(); // Log exception details for debugging
    }
        return node;
    }
    
    public void udateDatabase(int index,LinkedNode node){
        String updateQuery = "update datacollection set debit = ? ,credit = ?,descp = ?,amount = ?,acct_id = ? where counter = ?";

        try (Connection connection = databaseConnection.getConnection(); // Call the method from databaseConnection class
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){
                for (int i=0;i < index ;i++) {
                    // Ensure the arrays have values at this index
                        preparedStatement.setString(1, node.debit[i]);
                        preparedStatement.setString(2, node.credit[i]);
                        preparedStatement.setString(3, node.descp[i]);
                        preparedStatement.setDouble(4, node.amount[i]);
                        preparedStatement.setInt(5, LinkedNode.getAcctId());
                        preparedStatement.setInt(6, node.getDataCollectionCounter(i));
                        preparedStatement.addBatch();  // Add to batch
                }
            int[] rowsInserted = preparedStatement.executeBatch();
            if (rowsInserted.length > 0) {
                // System.out.println("A new user was inserted successfully!");
            }

        } catch (SQLException exception) {
            System.out.println("Error inserting user: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void deleteRecord(int counter){
        String sql = "Delete from datacollection where counter = ? and acct_id = ?";
        System.out.println("deletion process..."+counter);
        try(Connection conn = databaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, counter);
                statement.setInt(2,LinkedNode.getAcctId());
                
                int rowsInserted = statement.executeUpdate();
                if(rowsInserted>0){
                    System.out.println("successfull");
                }
            
        }catch(Exception e){

        }
    }

    public boolean readAccountant(String username, String password) {
        String sql = "SELECT acct_id FROM accountant WHERE acct_userName = ? AND acct_password = ?";
        
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // System.out.println("im here");
            statement.setString(1, username);
            statement.setString(2, password);
            // System.out.println(username+""+password);
            try (ResultSet result = statement.executeQuery()) {
                if(result.next()){
                    // System.out.println("inside a existed system");
                    LinkedNode.setAcctId(result.getInt("acct_id"));
                    // System.out.println(LinkedNode.getAcctId());
                    // System.out.println("exists");
                    return true;
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean readUser(String username,String password){
        String sql = "SELECT user_password, user_id FROM viewer WHERE user_userName ="+"'"+username+"'";
    
        try (Connection connection = databaseConnection.getConnection();
            Statement statement = connection.prepareStatement(sql)) {
    
            try (ResultSet result = statement.executeQuery(sql)) {
                // Use while loop to iterate over ResultSet rows
                if(result.next() && password.equals(result.getString("user_password"))) {
                    retriveRelation(result.getInt("user_id"));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log exception details for debugging
        }   
        return false;
    }
    
    public boolean createUser(String username, String password, String userType) {
        String query = null,checkSql=null;
    
        // Determine the appropriate query based on userType
        if ("Accountant".equals(userType)) {
            query = "INSERT INTO accountant (acct_userName, acct_password) VALUES (?, ?)";
            checkSql = "select acct_userName from accountant where acct_userName=?";
        } else if ("User".equals(userType)) {
            query = "INSERT INTO viewer (user_userName, user_password) VALUES (?, ?)";
            checkSql = "select user_userName from viewer where user_userName=?";
        } else {
            System.out.println("Invalid user type!");
            return false;
        }
    
        // Use PreparedStatement for parameterized query
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Set query parameters
            statement.setString(1, username);
            statement.setString(2, password);
            
            
            try(PreparedStatement checkStatement = connection.prepareStatement(checkSql)){
                checkStatement.setString(1, username);
                try(ResultSet rst = checkStatement.executeQuery()){
                    if(!rst.next()){
                        int rowsInserted = statement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println(rowsInserted);
                            System.out.println("A new user was inserted successfully!");
                            return true;
                        }
                    }else{

                    }
                }
            }
            
        } catch (SQLException e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
        return false;
    }

    public String createRelation(String username, String acctName){
        String acctSql = "Select acct_id from accountant where acct_username = ?";
        String userSql = "select user_id from viewer where user_username=?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement acctStm = conn.prepareStatement(acctSql);
             PreparedStatement userStm = conn. prepareStatement(userSql)) {

                acctStm.setString(1, acctName);
                userStm.setString(1, username);
                try(ResultSet acctRst = acctStm.executeQuery();ResultSet userRst = userStm.executeQuery()) {
                    if(acctRst.next() && userRst.next()){
                        String sql = "insert into relation(user_id,acct_id) values (?,?)";
                        try(PreparedStatement stm = conn.prepareStatement(sql)) {
                            stm.setInt(1, userRst.getInt("user_id"));
                            stm.setInt(2, acctRst.getInt("acct_id"));
                            int rowsAffected = stm.executeUpdate();
                            if(rowsAffected>0) {
                                return "true";
                            }else{
                                return "error in inserting the rows.";
                            }
                        }
                    }else{
                        return "Error on finding the username or accountant name";
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        return null;
    }

    public String retriveRelation(int user_id){
        System.out.println("retriving relation");
        String relationSql ="Select acct_id from relation where user_id=?";

        try(Connection connection = databaseConnection.getConnection();
        PreparedStatement acctStm = connection.prepareStatement(relationSql)) {
            acctStm.setInt(1, user_id);
            try(ResultSet acctId = acctStm.executeQuery()) { 
                if(acctId.next()){
                    System.out.println("accountant found");
                    System.out.println(LinkedNode.getAcctId());
                    LinkedNode.setAcctId(acctId.getInt("acct_id"));
                    System.out.println(LinkedNode.getAcctId());
                }
            }
        }catch(Exception e){

        }

        return null;
    }

    public void printAll(int index,LinkedNode node) {
        double total=0;
        System.out.printf("%-15s %-15s %-15s %15s%n", "Debit", "Credit", "Description", "Amount");
        for(int i=0;i < index ;i++){
           if(node.credit[i] != null){
                System.out.printf("%-15s %-15s %-15s %15.2f%n", node.debit[i], node.credit[i], node.descp[i], node.amount[i]);
                total+=node.amount[i];
           }else{
                break;
           }
        }
        System.out.println("Total amount = "+total);
    }

    public static void main(String[] args) {
        // databaseAction obj = new databaseAction();
        // LinkedNode node = new LinkedNode();
        // obj.read(node);
    }
}
