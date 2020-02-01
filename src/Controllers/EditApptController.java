/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Appointment;
import Models.Customer;
import Utils.Query;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class EditApptController implements Initializable {
    String userName;
    String q;
    String s;
    String c;
    String city;
    LocalDateTime now = LocalDateTime.now();
    ZonedDateTime zStartdt = null;
    ZonedDateTime zEnddt = null;
    ZonedDateTime localStartTime = null;
    ZonedDateTime localEndTime = null;
    Instant zStartgmt = null;
    Instant zEndgmt = null;
    LocalDateTime date;
    Appointment newAppt;
    Customer newCust;
    int custId;
    int apptId;
    int userId;
    int cityId;
    int custSearch = -1;
    int custSelect = -1;
    int newAppointment = -1;
    int apptSearch = -1;
    int apptCheck = 0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
    DateTimeFormatter timeOnly = DateTimeFormatter.ofPattern("HH:mm");
    
    public interface clear {
        String clear();
    }
    
    public interface validate {
        Boolean validate();
    }
    
    public interface apptVal {
        int apptVal();
    }

    @FXML
    private TextField ApptID;
    @FXML
    private TextField CustomerName;
    @FXML
    private TextField Description;
    @FXML
    private TextField ApptType;
    @FXML
    private TextField ApptStart;
    @FXML
    private DatePicker ApptDate;
    @FXML
    private TextField ApptEnd;
    @FXML
    private TextField ApptLoc;
    @FXML
    private TextField ApptContact;
    @FXML
    private TextField URL;
    @FXML
    private Button New;
    @FXML
    private Button Save;
    @FXML
    private Button Cancel;
    @FXML
    private TextField ApptSearch;
    @FXML
    private Button ApptSearchButton;
    @FXML
    private TableView<Appointment> ApptList;
    @FXML
    private TextField CustomerSearch;
    @FXML
    private Button CustomerSearchButton;
    @FXML
    private TableView<Customer> CustomerList;
    @FXML
    private Button Delete;
    @FXML
    private Button Close1;
    
    private ObservableList<Appointment> appointment = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentSearch = FXCollections.observableArrayList();
    private ObservableList<Customer> customer = FXCollections.observableArrayList();
    private ObservableList<Customer> customerSearch = FXCollections.observableArrayList();



    EditApptController(String userName) {
        this.userName = userName;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateAppointmentTable();
        generateCustomerTable();
    }

private void generateAppointmentTable(){
        appointment.clear();
        q = "select\n" +
            "a.appointmentId Id,\n" +
            "c.customerName Name,\n" +
            "a.start Start,\n" +
            "a.end End,\n" +
            "ad.phone Phone\n" +
            "\n" +
            "from\n" +
            "appointment a join\n" +
            "customer c on a.customerId = c.customerId join\n" +
            "address ad on c.addressId = ad.addressId";
        ResultSet result = Query.makeQuery(q);
        try {
            while(result.next()){
                appointment.add(new Appointment(result.getString("Name"),result.getTimestamp("Start").toLocalDateTime(),result.getTimestamp("End").toLocalDateTime(),result.getString("Phone"),result.getInt("Id")));
                ApptList.setItems(appointment);
                ApptList.refresh();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

private void generateCustomerTable(){
        customer.clear();
        q = "select\n" +
            "cu.customerId Id,\n" +
            "cu.customerName Name,\n" +
            "ci.city City,\n" +
            "co.country Country,\n" +
            "ad.phone Phone\n" +
            "\n" +
            "from\n" +
            "U05Hru.customer cu join\n" +
            "U05Hru.address ad on cu.addressId = ad.addressId join\n" +
            "U05Hru.city ci on ad.cityId = ci.cityId join\n" +
            "U05Hru.country co on ci.countryId = co.countryId";
        ResultSet result = Query.makeQuery(q);
        try {
            while(result.next()){
                customer.add(new Customer(result.getInt("Id"),result.getString("Name"),result.getString("City"),result.getString("Country"),result.getString("Phone")));
                CustomerList.setItems(customer);
                CustomerList.refresh();
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void New(MouseEvent event) throws SQLException {
        if(custSelect == 1){
            custSelect = custSelect * -1;
        }
        if(newAppointment == -1){
            newAppointment *= -1;
        }
        clear.clear();
        q = "select a.appointmentId from appointment a order by appointmentId";
        ResultSet result = Query.makeQuery(q);
        while(result.next()){
            apptId = result.getInt("appointmentId") + 1;
        }
        ApptID.setText(String.valueOf(apptId));
    }

    @FXML
    private void Save(MouseEvent event) throws SQLException {
        
        if(validate.validate()){
            if(apptVal.apptVal() != 0) {
                return;
            }
            q = "select user.userId from user where user.userName = '" + userName + "'";
            ResultSet result = Query.makeQuery(q);
            try {
                while(result.next()){
                    userId = result.getInt("userId");
                }
            } catch (SQLException ex) {
                Logger.getLogger(EditApptController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(newAppointment == -1){
                Update();
            } else{
                NewAppt();
            }
        } else {
            alertMessage("Could not save");
        }
  
    }
    
    private void Update(){
        LocalTime startTime = LocalTime.parse(ApptStart.getText().trim());
        LocalTime endTime = LocalTime.parse(ApptEnd.getText().trim());
        LocalDate date = ApptDate.getValue();
        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);
        if(validate.validate()){
            if(apptVal.apptVal() != 0){
                return;
            }   else {
                    q = "update U05Hru.appointment a set \n" +
                    "a.customerId = '" + custId + "', \n" +
                    "a.userId = '" + userId + "', \n" +
                    "a.description = '" + Description.getText().trim() + "', \n" +
                    "a.type = '" + ApptType.getText().trim() + "', \n" +
                    "a.location = '" + ApptLoc.getText().trim() + "', \n" +
                    "a.contact = '" + ApptContact.getText().trim() + "', \n" +
                    "a.url = '" + URL.getText().trim() + "', \n" +
                    "a.start = '" + start + "', \n" +
                    "a.end = '" + end + "', \n" +
                    "a.lastUpdateBy = '" + userId + "' \n" +
                    "where a.appointmentId = '" + ApptID.getText() + "'";
                    Query.makeQuery(q);
                    generateAppointmentTable();
                    System.out.println("Appt Updated");
                    clear.clear();
                }
        }
    }
    
    private void NewAppt() throws SQLException{
        LocalTime startTime = LocalTime.parse(ApptStart.getText().trim());
        LocalTime endTime = LocalTime.parse(ApptEnd.getText().trim());
        LocalDate date = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);
        ApptID.setText(String.valueOf(apptId));
        if(validate.validate()){
            if(apptVal.apptVal() != 0){
                return;
            } else {
            q = "insert into appointment (customerId,userId,title,description,location,contact,type,url,start,end,createDate,createdBy,lastUpdateBy) values('" + custId + "','" + userId + "','Title','" + Description.getText().trim() + "','" + ApptLoc.getText().trim() + "','" + ApptContact.getText().trim() + "','" + ApptType.getText().trim() + "','" + URL.getText().trim() + "','" + start + "','" + end + "','" + date + "','" + userName + "','" + userName + "')";
            Query.makeQuery(q);
            System.out.println("Appt added");
            }
        }
        generateAppointmentTable();
        clear.clear();
    }
   
    @FXML
    private void Cancel(MouseEvent event) {
        if(custSelect == 1){
            custSelect = custSelect * -1;
        }
        if(newAppointment == 1) {
            newAppointment = newAppointment * -1;
        }
       clear.clear(); 
    }
    
    @FXML
    private void ApptSearch(MouseEvent event) {
        if (apptSearch < 0){
            appointmentSearch.clear();
            for(Appointment a: appointment){
                if (a.getCustomerName().toLowerCase().contains(ApptSearch.getText().trim().toLowerCase())){
                    appointmentSearch.add(a);
                }
            }
            ApptList.setItems(appointmentSearch);
            ApptList.refresh();
            ApptSearchButton.setText("Cancel");
        } else {
            appointmentSearch.clear();
            ApptSearchButton.setText("Search");
            ApptSearch.clear();
            ApptList.setItems(appointment);
            ApptList.refresh();
        }
        apptSearch = apptSearch * -1; 
    }

    @FXML
    private void CustomerSearch(MouseEvent event) {
        if (custSearch < 0){
            customerSearch.clear();
            for(Customer c: customer){
                if (c.getName().toLowerCase().contains(CustomerSearch.getText().trim().toLowerCase())){
                    customerSearch.add(c);
                }
            }
            CustomerList.setItems(customerSearch);
            CustomerList.refresh();
            CustomerSearchButton.setText("Cancel");
        } else {
            customerSearch.clear();
            CustomerSearchButton.setText("Search");
            CustomerSearch.clear();
            CustomerList.setItems(customer);
            CustomerList.refresh();
        }
        custSearch = custSearch * -1;
    }

    @FXML
    private void Delete(MouseEvent event) {
        newAppt = ApptList.getSelectionModel().getSelectedItem();
        q = "DELETE from appointment WHERE appointmentId = '" + newAppt.getAppointmentId() + "'";        
        Query.makeQuery(q);
        clear.clear();
        generateAppointmentTable();
    }
    
    @FXML
    private void ApptSelect(MouseEvent event) throws SQLException {
        newAppt = ApptList.getSelectionModel().getSelectedItem();
        LocalTime setStart = null;
        LocalTime setEnd = null;
        LocalDate setDate;
        q = "select\n" +
            "ap.appointmentId Id,\n" +
            "cu.customerName Name,\n" +
            "ap.customerId,\n" +
            "ap.type Type,\n" +
            "ap.description Description,\n" +
            "ap.start Start,\n" +
            "ap.end End,\n" +
            "ap.location Location,\n" +                
            "ap.contact Contact,\n" +
            "ad.cityId City,\n" +
            "ap.url Url\n" +
            "\n" +
            "from\n" +
            "U05Hru.appointment ap join\n" +
            "U05Hru.customer cu on ap.customerId = cu.customerId join\n" +
            "U05Hru.address ad on cu.addressId = ad.addressId\n" +
            "\n" +
            "where ap.appointmentId = '" + newAppt.getAppointmentId() + "'" ;
        ResultSet result = Query.makeQuery(q);
        try{
            while(result.next()){
                setStart = result.getTime("Start").toLocalTime();
                setEnd = result.getTime("End").toLocalTime();
                setDate = result.getDate("Start").toLocalDate();
                ApptID.setText(String.valueOf(result.getInt("Id")));
                CustomerName.setText(result.getString("Name"));
                Description.setText(result.getString("Description"));
                ApptType.setText(result.getString("Type"));
                ApptDate.setValue(setDate);
                ApptStart.setText(setStart.format(timeOnly));
                ApptEnd.setText(setEnd.format(timeOnly));
                ApptLoc.setText(result.getString("Location"));
                ApptContact.setText(result.getString("Contact"));
                URL.setText(result.getString("Url"));
                custId = result.getInt("customerId");
                cityId = result.getInt("City");
                
            }
           
            
        } catch (SQLException ex) {
            Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        convertDate(cityId);
    }
    
    @FXML
    private void CustomerSelect(MouseEvent event){
        if (custSelect == -1){
        newCust = CustomerList.getSelectionModel().getSelectedItem();
        custId = newCust.getId();
        CustomerName.setText(newCust.getName());
        custSelect *= -1;
        }
    }

    @FXML
    private void Close(MouseEvent event) throws IOException {
        clear.clear();
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
    
    private void convertDate(int city){
        LocalDate apptDate = ApptDate.getValue();
        LocalTime apptStartTime = LocalTime.parse(ApptStart.getText());
        LocalTime apptEndTime = LocalTime.parse(ApptEnd.getText());
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
        ZonedDateTime zStartdt = ZonedDateTime.of(apptDate,apptStartTime,zone);
        ZonedDateTime zEnddt = ZonedDateTime.of(apptDate,apptEndTime, zone);
        Instant zStartgmt = zStartdt.toInstant();
        Instant zEndgmt = zEnddt.toInstant();
        ZonedDateTime localStart = zStartdt.toInstant().atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime localEnd = zEnddt.toInstant().atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());
        ApptStart.setText(String.valueOf(localStart.getHour()) + ":" + String.valueOf(localStart.getMinute()));
        ApptEnd.setText(String.valueOf(localEnd.getHour()) + ":" + String.valueOf(localEnd.getMinute()));
        if(localStart.getHour() < apptStartTime.getHour()){
            ApptDate.setValue(apptDate.plusDays(1));
        }
        String start = ApptStart.getText();
        String end = ApptEnd.getText();
        if(start.equals("[0-9]:[0-9][0-9]")){
                    ApptStart.setText("0" + start);
        }
        if(end.equals("[0-9]:[0-9][0-9]")){
            ApptEnd.setText("0" + end);
        } 
        return;
    }
    
    private void alertMessage(String field){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Invalid Appointment Data");
           alert.setContentText(field);
           alert.showAndWait();
        }
    
    
    //Lambdas used to reduce code needed, make program more efficient
    clear clear = () -> {
        ApptID.clear();
        CustomerName.clear();
        Description.clear();
        ApptType.clear();
        ApptDate.setValue(null);
        ApptStart.clear();
        ApptEnd.clear();
        ApptLoc.clear();
        ApptContact.clear();
        URL.clear(); 
       return null;
    };
    
    validate validate = () -> {
       if(CustomerName.getText().trim().length() == 0) {
           alertMessage("Name must not be blank");
           return false;
       } 
       if(ApptType.getText().trim().length() == 0) {
           alertMessage("Type must not be blank");
           return false;
       }
       if(ApptStart.getText().trim().length() == 0){
           alertMessage("Start must not be blank");
           return false;
       }
       if(!ApptStart.getText().trim().matches("([01][0-9]|2[0-3]):[0-5][0-9]")){
           alertMessage("Start time must be in HH:MM format");
           return false;
       }
       if(!ApptEnd.getText().trim().matches("([01][0-9]|2[0-3]):[0-5][0-9]")){
           alertMessage("End time must be in HH:MM format");
           return false;
       }
       if(ApptEnd.getText().trim().length() == 0){
           alertMessage("End must not be blank");
           return false;
       }
       if(ApptLoc.getText().trim().length() == 0){
           alertMessage("Location must not be blank");
           return false;
       }
       if(ApptContact.getText().trim().length() == 0){
           alertMessage("Contact must not be blank");
           return false;
       }
       if(URL.getText().trim().length() == 0){
           alertMessage("URL must not be blank");
           return false;
       }
        LocalTime apptStarts = LocalTime.parse(ApptStart.getText());
        LocalTime apptEnds = LocalTime.parse(ApptEnd.getText());
        if(apptEnds.isBefore(apptStarts)){
           alertMessage("Start must be before end. Remember to use 24hr format for times.");
           return false;
       }
       return true;
    };
    
    apptVal apptVal = () -> {
        apptCheck = 0;
        ZoneId zone = null;
        //check business hours (M-F 8-5 in CUSTOMER time zone)
        if(newAppointment == -1){
            q = "select ci.cityId city, ap.start start, ap.end end from appointment ap join customer cu on ap.customerId = cu.customerId join address ad on cu.addressId = ad.addressId join city ci on ad.cityId = ci.cityId where ap.appointmentId = '" + ApptID.getText() + "'";
            try{
                ResultSet result = Query.makeQuery(q);
                while(result.next()){
                    cityId = result.getInt("city");
                }
                switch(cityId){
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
            } catch (SQLException ex) {
                Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
           q = "select ci.cityId city from customer cu join address ad on cu.addressId = ad.addressId join city ci on ad.cityId = ci.cityId where cu.customerId = '" + CustomerList.getSelectionModel().getSelectedItem().getId() + "'";
            try{
                ResultSet result = Query.makeQuery(q);
                while(result.next()){
                    cityId = result.getInt("city");
                }
                switch(cityId){
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
            } catch (SQLException ex) {
                Logger.getLogger(ViewApptController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        ZonedDateTime apptStart = ZonedDateTime.of(ApptDate.getValue(), LocalTime.parse(ApptStart.getText().trim()), zone);
        ZonedDateTime apptEnd = ZonedDateTime.of(ApptDate.getValue(), LocalTime.parse(ApptEnd.getText().trim()), zone);
        if(apptStart.getHour() < 8 || apptStart.getHour() > 17){
            alertMessage("Your start time: " + apptStart.getHour() + " is outside the client's local business hours (M-F 8-5)");
            apptCheck += 1;
        }
        if(apptStart.getDayOfWeek().equals(SATURDAY) || apptStart.getDayOfWeek().equals(SUNDAY)){
            alertMessage("Your start day: " + apptStart.getDayOfWeek() + " is outside the client's local business hours (M-F 8-5)");
            apptCheck += 1;
        }
        
        //check if appt overlaps with other appt        
        for(Appointment a: appointment){
            if(apptStart.isAfter(ZonedDateTime.of(a.getStartl(), zone)) && apptStart.isBefore(ZonedDateTime.of(a.getEndl(), zone))){
                alertMessage("Your start time conflicts with another appointment (based on both appointment's local times");
                apptCheck += 1;
            }
            if(apptEnd.isAfter(ZonedDateTime.of(a.getStartl(), zone)) && apptEnd.isBefore(ZonedDateTime.of(a.getEndl(), zone))){
                alertMessage("Your end time conflicts with another appointment (based on both appointment's local times)");
                apptCheck += 1;
            }
        }
        
        return apptCheck;
    };
            
    
    
}
