/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package Models;
//
//import Utils.DBConnection;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
///**
// *
// * @author Dev
// */
//public class DFSoftware2 extends Application {
//    
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LogIn.fxml"));
//        Controllers.LogInController controller = new Controllers.LogInController();
//        loader.setController(controller);
//        Parent root = loader.load();                
//        Scene scene = new Scene(root);        
//        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.show();
//    }
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        DBConnection.startConnection();
//        launch(args);
//    }
//    
//}
