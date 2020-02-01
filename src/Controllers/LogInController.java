/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Utils.DBConnection;
import Utils.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.Locale;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class LogInController implements Initializable {
    Locale language;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button buttonLogIn;
    @FXML 
    private Label labelLogIn;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setLang();
    } 
    
    private void setLang(){
        language = Locale.getDefault();
        if(language.getLanguage().equals(new Locale("es").getLanguage())){
            username.setText("Nombre de usuario");
            password.setText("Contraseña");
            buttonLogIn.setText("Iniciar sesión");
            labelLogIn.setText("Iniciar sesión");
        }
    }

    @FXML
    private void logIn(MouseEvent event) throws IOException, Exception {
            //Test Connection (see console for successful connection message)
            DBConnection.startConnection();
            
            //Write SQL statement
            String sqlStatement = "SELECT * FROM user WHERE user.userName = '" + username.getText().trim() + "' AND user.password = '" + password.getText().trim() + "'";
            
            //Execute Statement and create ResultSet object
            ResultSet result = Query.makeQuery(sqlStatement);
            
            //Get all records from ResultSet object
            int count = 0;
            while(result.next()) {
                count +=1;
            }
            if(count == 1){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ViewAppt.fxml"));
                Controllers.ViewApptController controller = new Controllers.ViewApptController(username.getText().trim());
                loader.setController(controller);
                Parent root = loader.load();                
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } else{
                invalid();

            }
    }
    private void invalid(){
            
        if(language.getLanguage().equals(new Locale("es").getLanguage())){
            alertMessage("Nombre de usuario o contraseña incorrecto");
        } else{
            alertMessage("Incorrect username or password");
        }
    }
    
    private void alertMessage(String field){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           if(language.getLanguage().equals(new Locale("es").getLanguage())){
                    alert.setTitle("Incapaz de iniciar sesión");
                } else {
                    alert.setTitle("Unable to log in");
                }
           alert.setContentText(field);
           alert.showAndWait();
        }
    
}
