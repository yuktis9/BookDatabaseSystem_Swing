/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookClientGui.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author dheeraj
 */
public class QueryData {
    
    private EstablishConnection establishConnection = new EstablishConnection();
    
    private static Connection connect;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
  
    public void setConnection(Connection connect){
        QueryData.connect = connect;
    }
    public String[] getTableName(){
        
       String tableNames [] = new String[3];
        
       String url = "select table_name from information_schema.tables where table_schema='s_dmukati'";
       
       establishConnection = new EstablishConnection();

        try {
            statement = (Statement) connect.createStatement();
            resultSet = statement.executeQuery(url);

            int i = 0;
            while (resultSet.next()) {
                tableNames[i] = resultSet.getString(1);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableNames;
    }
    
    public Object[][] getTableContent(String tableName){
        
        String sql = "select * from " + tableName;
        Object[][] tableContent = null;
        try {
            
            statement = (Statement) connect.createStatement();
            resultSet = statement.executeQuery(sql);

            int tableSize = 0;
            while (resultSet.next()) {
                tableSize++;
            }
            
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int colNo = rsmd.getColumnCount();
            
            tableContent = new Object[tableSize][colNo+1];
            
            int column = 0;
            while (resultSet.next()) {
                int row, pk;
                for (row = 0, pk = 1; row < colNo; row++ , pk++) {
                    tableContent [column][row] = resultSet.getString(pk);
                }
                tableContent [column][row] = false;
                column++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableContent;
    }
    
    public String [] getColumnNames(String tableName, String pageName){
        
       String tableNames [] = null;
       
       String sql = "select * from " + tableName;
       
       establishConnection = new EstablishConnection();

        try {
            statement = (Statement) connect.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int colNo = rsmd.getColumnCount();
            tableNames = new String[colNo + 1];
            
            int k , i;
            for (i = 1, k = 0; i < colNo + 1; i++, k++) {              
                 tableNames[k] = rsmd.getColumnName(i);

            }
            if(pageName.equals("MaintainData")){
                tableNames [k] = "Delete";
            }     
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableNames;
    }
    
    public void deleteRow(String tableName, String columnName, String selectedIds){
        
       String deleteSQL = "DELETE FROM "+tableName+" WHERE "+columnName+" IN ("+selectedIds+")";
       String deleteAuthorISBN = "DELETE FROM AuthorISBN WHERE "+columnName+" IN ("+selectedIds+")";
       
       establishConnection = new EstablishConnection();

        try {
            preparedStatement = (PreparedStatement) connect.prepareStatement(deleteAuthorISBN); 
            preparedStatement = (PreparedStatement) connect.prepareStatement(deleteSQL);
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateRecord(int id, String value, String tableName, String columnName, String idColumnName){
        
       String updateSQL = "UPDATE "+tableName+" SET "+columnName+"=? where "+idColumnName+"= ?";
       
       establishConnection = new EstablishConnection();

        try {
            preparedStatement = (PreparedStatement) connect.prepareStatement(updateSQL);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            
            preparedStatement.executeUpdate();
            System.out.println("Row Deleted succesfully!");
            
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int addTitle(String tableName, int ISBN, String title, int editionNo, int copyright){
        
        String deleteSQL = "INSERT INTO "+tableName+"(`ISBN`, `Title`, `EditionNumber`, `Copyright`) values (?,?,?,?)";
       
       establishConnection = new EstablishConnection();

        try {
            preparedStatement = (PreparedStatement) connect.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, ISBN);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, editionNo);
            preparedStatement.setInt(4, copyright);
            
            return preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int addAuthor(String tableName, String firstName, String lastName){
       
       
       String addSQL = "INSERT INTO "+tableName+"(`FirstName`, `LastName`) values (?,?)";
       
       establishConnection = new EstablishConnection();

        try {
            preparedStatement = (PreparedStatement) connect.prepareStatement(addSQL);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            
            return preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Map<String, String> searchData(String tableName, String columnName, String searchChar){
       
        Map<String, String> searchedData = new HashMap<String, String>();
        String searchSql = "SELECT * FROM "+tableName+" WHERE "+columnName+" LIKE ?";
       
       establishConnection = new EstablishConnection();

        try {
            preparedStatement = (PreparedStatement) connect.prepareStatement(searchSql);
            preparedStatement.setString(1,  searchChar + "%");

            resultSet = preparedStatement.executeQuery();
           
            while (resultSet.next()) {
                
                if(tableName != null && tableName.equals("Titles")){
                    
                    String isbn  = resultSet.getString(1);
                    String title = resultSet.getString(2);
                    if(columnName != null && columnName.equals("ISBN")){
                        
                        searchedData.put(isbn, String.valueOf(isbn));
                    }else{
                        searchedData.put(isbn, title);
                    }
                }else if(tableName != null && tableName.equals("Authors")){
                    
                    String authorId  = resultSet.getString(1);
                    System.out.println("AuthorId: "+authorId);
                    String fisrtName = resultSet.getString(2);
                    String lastName = resultSet.getString(3);
                    searchedData.put(authorId, fisrtName +" "+lastName);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }  
        System.out.println("searchedData Size: "+searchedData);
        return searchedData;
    }
    
    public Object[][] retrieveData(String retrieveQuery){
        
        Object[][] tableContent = null;
        try {
            
            statement = (Statement) connect.createStatement();
            resultSet = statement.executeQuery(retrieveQuery);

            int tableSize = 0;
            while (resultSet.next()) {
                tableSize++;
            }
            
            resultSet = statement.executeQuery(retrieveQuery);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int colNo = rsmd.getColumnCount();
            
            tableContent = new Object[tableSize][colNo+1];
            
            int column = 0;
            while (resultSet.next()) {
                int row, pk;
                for (row = 0, pk = 1; row < colNo; row++ , pk++) {
                    tableContent [column][row] = resultSet.getString(pk);
                }
                column++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableContent;
    }
    
    public String [] getColumnNamesforSearchData(String sql){
        
       String tableNames [] = null;
    
       establishConnection = new EstablishConnection();

        try {
            statement = (Statement) connect.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int colNo = rsmd.getColumnCount();
            tableNames = new String[colNo + 1];
            
            int k , i;
            for (i = 1, k = 0; i < colNo + 1; i++, k++) {              
                 tableNames[k] = rsmd.getColumnName(i);

            }   
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableNames;
    }
    
    public void addAuthorISBN(String authorID, String ISBN){
        
        String sql = "INSERT INTO `s_dmukati`.`AuthorISBN` (`AuthorID`, `ISBN`) VALUES (?, ?)";
        try {
            preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
            preparedStatement.setString(1,  authorID);
            preparedStatement.setString(2,  ISBN);
            System.out.println("sql: "+sql);
            preparedStatement.executeUpdate();   
        } catch (SQLException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
