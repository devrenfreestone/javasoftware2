/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.ResultSet;
import java.sql.Statement;
import static Utils.DBConnection.conn;

/**
 *
 * @author Dev
 */
public class Query {
    private static String query;
    private static Statement stmt;
    private static ResultSet result;

    public static ResultSet makeQuery(String q){
            query = q;

            try{
                    stmt = conn.createStatement();

                    if(query.toLowerCase().startsWith("select"))
                            result = stmt.executeQuery(query);

                    if(query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update"))
                            stmt.executeUpdate(query);
            }

            catch(Exception ex){
                    System.out.println("Error: " + ex.getMessage());
            }
            
            return result;
    }

//    public static ResultSet getResultSet(){
//            return result;
//    }
    
}
