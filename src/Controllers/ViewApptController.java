/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Appointment;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import Utils.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;



import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class ViewApptController implements Initializable {
    String q;
    String s;
    String c;
    String userName;
    String start;
    String end;
    int city = 0;
    LocalDateTime date = LocalDateTime.now();
    Appointment newAppt;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    ZonedDateTime zStartdt = null;
    ZonedDateTime zEnddt = null;
    ZonedDateTime localStartTime = null;
    ZonedDateTime localEndTime = null;
    Instant zStartgmt = null;
    Instant zEndgmt = null;


    @FXML
    private RadioButton All;
    @FXML
    private ToggleGroup Appt;
    @FXML
    private RadioButton Week;
    @FXML
    private RadioButton Month;
    @FXML
    private Button EditAppt;
    @FXML
    private Button EditCustomers;
    @FXML
    private Button ViewReports;

    @FXML
    private TableView<Appointment> AppointmentList;
    
    private ObservableList<Appointment> appointment = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentTime = FXCollections.observableArrayList();
    

    ViewApptController(String userName) {
       this.userName = userName; 
       
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateAppointmentTable();
        checkFifteen();
        try {
            log(userName + " " + date);
        } catch (IOException ex) {
            Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private void generateAppointmentTable() {
        appointment.clear();
        appointmentTime.clear();
        q = "select\n" +
            "a.appointmentId Id,\n" +
            "c.customerName Name,\n" +
            "a.description Description,\n" +
            "a.start Start,\n" +
            "a.end End,\n" +
            "a.location Location,\n" +
            "ad.cityId City,\n" +
            "u.userName userName\n" +
            "\n" +
            "from\n" +
            "appointment a join\n" +
            "customer c on a.customerId = c.customerId join\n" +
            "address ad on c.addressId = ad.addressId join\n" +
            "user u on a.userId = u.userId";
        ResultSet result = Query.makeQuery(q);
        try {
            while(result.next()){
                city = result.getInt("City");
                LocalDate apptDate = result.getDate("Start").toLocalDate();
                LocalTime apptStartTime = result.getTime("Start").toLocalTime();
                LocalTime apptEndTime = result.getTime("End").toLocalTime();
                ZoneId zone = null;
                switch(city){
                    case 4:
                        zone = ZoneId.of("America/Mexico_City");
                        break;  
                    case 1:
                        zone = ZoneId.of("America/Phoenix");
                        break;
                    case 3:
                        zone = ZoneId.of("Europe/London");
                        break;
                    case 2:
                        zone = ZoneId.of("America/New_York");
                        break;
                }
                zStartdt = ZonedDateTime.of(apptDate,apptStartTime,zone);
                zEnddt = ZonedDateTime.of(apptDate,apptEndTime, zone);
                ZonedDateTime localStart = zStartdt.toInstant().atZone(ZoneId.of(ZoneId.systemDefault().toString()));
                ZonedDateTime localEnd = zEnddt.toInstant().atZone(ZoneId.of(ZoneId.systemDefault().toString()));
                if(localStart.getHour() < apptStartTime.getHour()){
                    apptDate = apptDate.plusDays(1);
                }
                start = String.valueOf(apptDate) + " " + String.valueOf(localStart.getHour()) + ":" + String.valueOf(localStart.getMinute());
                end = String.valueOf(apptDate) + " " + String.valueOf(localEnd.getHour()) + ":" + String.valueOf(localEnd.getMinute());
                appointment.add(new Appointment(result.getInt("Id"),result.getString("Name"),result.getString("Description"),start,end,result.getString("Location"),result.getString("userName")));
                AppointmentList.setItems(appointment);
                AppointmentList.refresh();
                appointmentTime.add(new Appointment(result.getString("Name"),result.getTimestamp("Start").toLocalDateTime(),result.getTimestamp("End").toLocalDateTime(),result.getString("Name"),result.getInt("Id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void checkFifteen(){
        for(Appointment a: appointmentTime){
            LocalDateTime start = a.getStartl();
            if(start.isAfter(date) && start.isBefore(date.plusMinutes(15))){
                alertMessage("You have an appointment with " + a.getCustomerName() + " at " + a.getStartl().getHour() + ":" + a.getStartl().getMinute());
            }
        }
    }

    @FXML
    private void EditAppt(MouseEvent e) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/EditAppt.fxml"));
        Controllers.EditApptController controller = new Controllers.EditApptController(userName); 
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void EditCustomers(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/EditCustomer.fxml"));
        Controllers.EditCustomerController controller = new Controllers.EditCustomerController(userName); 
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void ViewReports(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ViewReport.fxml"));
        Controllers.ViewReportController controller = new Controllers.ViewReportController(userName); 
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    private void All(MouseEvent event) throws IOException, SQLException{
        appointmentTime.clear();
        generateAppointmentTable();
    }
    
    @FXML
    private void Week(MouseEvent event) throws IOException, SQLException{
        appointmentTime.clear();
        q = "select\n" +
            "a.appointmentId Id,\n" +
            "c.customerName Name,\n" +
            "a.description Description,\n" +
            "a.start Start,\n" +
            "a.end End,\n" +
            "a.location Location,\n" +
            "u.userName userName\n" +
            "\n" +
            "from\n" +
            "appointment a join\n" +
            "customer c on a.customerId = c.customerId join\n" +
            "user u on a.userId = u.userId\n" +
            "where a.start between now() and adddate(now(),7)";
        ResultSet result = Query.makeQuery(q);
        appointmentTime.clear();
        try {
            while(result.next()){
                LocalDateTime start = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
                appointmentTime.add(new Appointment(result.getInt("Id"),result.getString("Name"),result.getString("Description"),start.format(formatter),end.format(formatter),result.getString("Location"),result.getString("userName")));
                AppointmentList.setItems(appointment);
                AppointmentList.refresh();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
        }
        AppointmentList.setItems(appointmentTime);
        AppointmentList.refresh();
    }
    
    @FXML
    private void Month(MouseEvent event) throws IOException, SQLException{
        appointmentTime.clear();
        q = "select\n" +
            "a.appointmentId Id,\n" +
            "c.customerName Name,\n" +
            "a.description Description,\n" +
            "a.start Start,\n" +
            "a.end End,\n" +
            "a.location Location,\n" +
            "u.userName userName\n" +
            "\n" +
            "from\n" +
            "appointment a join\n" +
            "customer c on a.customerId = c.customerId join\n" +
            "user u on a.userId = u.userId\n" +
            "where a.start between now() and adddate(now(),30)";
        ResultSet result = Query.makeQuery(q);
        appointmentTime.clear();
        try {
            while(result.next()){
                LocalDateTime start = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = result.getTimestamp("End").toLocalDateTime();
                appointmentTime.add(new Appointment(result.getInt("Id"),result.getString("Name"),result.getString("Description"),start.format(formatter),end.format(formatter),result.getString("Location"),result.getString("userName")));
                AppointmentList.setItems(appointment);
                AppointmentList.refresh();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
        }
        AppointmentList.setItems(appointmentTime);
        AppointmentList.refresh();
    }
    
    private void alertMessage(String field){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Appointment starting soon");
           alert.setContentText(field);
           alert.showAndWait();
        }
    
    private void log(String log) throws IOException{
        String fileName = "log.txt";
        FileWriter fwriter = new FileWriter(fileName, true);
        PrintWriter output = new PrintWriter(fwriter);
        output.println(log);
        System.out.println(log);
        output.close();
        System.out.println("File written.");

    }
    
}
