/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import static Utils.DBConnection.conn;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dev
 */
public class JDBCMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Load screen code
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LogIn.fxml"));
        Controllers.LogInController controller = new Controllers.LogInController();
        loader.setController(controller);
        Parent root = loader.load();                
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
    }
    
    public static void main(String[] args) {
        try{
            DBConnection.startConnection();
            
            //Create Statement Object
            Statement stmt = conn.createStatement();
            
            //Write SQL statement
            String sqlStatement = "SELECT * FROM user";
            
            //Execute Statement and create ResultSet object
            ResultSet result = stmt.executeQuery(sqlStatement);
            
            //Get all records from ResultSet object
//            while(result.next()) {
//                System.out.println(result.getInt("userId"));
//            }
            DBConnection.closeConnection();
            
            launch(args);
            
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
}
