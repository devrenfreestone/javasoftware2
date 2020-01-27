/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Appointment;
import Utils.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class ViewReportController implements Initializable {
    String userName;
    String q;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    interface set{
        String setQ();
    }


    @FXML
    private RadioButton Type;
    @FXML
    private ToggleGroup Appt;
    @FXML
    private RadioButton Consultant;
    @FXML
    private RadioButton Location;
    @FXML
    private Button Close;
    @FXML
    private TableView<Appointment> AppointmentList;
    
    private ObservableList<Appointment> appointment = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentSearch = FXCollections.observableArrayList();

    ViewReportController(String userName) {
        this.userName = userName;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setQ.setQ();
        generateAppointmentTable();
    }
    
    //lambda used to reduce code in methods and be more efficient.
    set setQ = () -> {
        q = "select\n" +
            "a.appointmentId Id,\n" +
            "c.customerName Name,\n" +
            "a.description Description,\n" +
            "a.start Start,\n" +
            "a.type Type,\n" +
            "a.location Location,\n" +
            "u.userName userName\n" +
            "\n" +
            "from\n" +
            "appointment a join\n" +
            "customer c on a.customerId = c.customerId join\n" +
            "user u on a.userId = u.userId";
        return null;
    };
    
    private void generateAppointmentTable(){
        appointment.clear();
            // where start <> time for checking if appt conflict, use timestamp (include ?'s)
            //where start >? and end <?
            //(look into prepared statement objects, timestamp objects can be inserted into ? on the previous line ch 10, online)
        ResultSet result = Query.makeQuery(q);
        try {
            while(result.next()){
                LocalDateTime start = result.getTimestamp("Start").toLocalDateTime();
                appointment.add(new Appointment(result.getString("Name"),result.getString("Description"),start.format(formatter),result.getInt("Id"),result.getString("Type"),result.getString("Location"),result.getString("userName")));
                AppointmentList.setItems(appointment);
                AppointmentList.refresh();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void Type(MouseEvent event) throws SQLException {
        q = q + " order by a.type";
        loadTable(q);
        setQ.setQ();
    }
    
    @FXML
    private void Consultant(MouseEvent event) throws SQLException {
        q = q + " order by u.userName";
        loadTable(q);
        setQ.setQ();
    }
    
    @FXML
    private void Location(MouseEvent event) throws SQLException {
        q = q + " order by a.location";
        loadTable(q);
        setQ.setQ();
    }
    
    private void loadTable(String q) throws SQLException {
            appointment.clear();
            ResultSet result = Query.makeQuery(q);
                while(result.next()){
                    LocalDateTime start = result.getTimestamp("Start").toLocalDateTime();
                    appointment.add(new Appointment(result.getString("Name"),result.getString("Description"),start.format(formatter),result.getInt("Id"),result.getString("Type"),result.getString("Location"),result.getString("userName")));
                    AppointmentList.setItems(appointment);
                    AppointmentList.refresh();
                }
    }

    @FXML
    private void Close(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ViewAppt.fxml"));
        Controllers.ViewApptController controller = new Controllers.ViewApptController(userName);
        loader.setController(controller);
        Parent root = loader.load();                
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
