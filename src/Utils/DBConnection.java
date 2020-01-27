/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;


public class DBConnection {
    
    //JDBC URL Parts
    private static final String protocol = "jdbc:";
    private static final String vendorName = "mysql:";
    private static final String ipAddress = "//3.227.166.251/U05Hru";
    
    //Concat JDBC String, Driver interface reference, username/password
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    public static Connection conn = null;
    
    private static final String driver = "com.mysql.jdbc.Driver";
    
    private static final String username = "U05Hru";
    private static String password = "53688503155";
    
    public static Connection startConnection(){
        try {
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(jdbcURL,username,password);
            System.out.println("Connection successful");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    } 
    
    public static void closeConnection() throws SQLException{
        conn.close();
        System.out.println("Connection closed");
    }
}
