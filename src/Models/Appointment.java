/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dev
 */
public class Appointment {
    int id;
    String name;
    String description;
    String start;
    String end;
    String type;
    String location;
    String user;
    String phone;
    String url;
    
    int customerId;
    int userId;
    int cityId;
    String title;
    String contact;
    Timestamp createDate;
    String createdBy;
    Timestamp lastUpdate;
    String lastUpdateBy;
    Timestamp start1;
    Timestamp end1;
    
    public int getCityId() {
        return cityId;
    }  

    public String getUrl() {
        return url;
    }

    public Timestamp getStart1(){
        return start1;
    }
    
    public Timestamp getEnd1(){
        return end1;
    }
    
    public String getPhone() {
        return phone;
    }
    
    LocalDateTime startTime;

    public int getAppointmentId() {
        return id;
    }

    public String getCustomerName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }
    
    public String getType(){
        return type;
    }
    
    public String getStartFormatted(){
        return start.toString();
    }

    public String getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }

    public String getUserName() {
        return user;
    }
    
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }
    public static void addAppointment(Appointment appointment){
        allAppointments.add(appointment);
    }
    
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public Appointment(int id, String name, String description, String start, String end, String location, String user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.location = location;
        this.user = user;
    }
    
    public Appointment(String name, String description, String start, int id,  String type, String location, String user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.type = type;
        this.location = location;
        this.user = user;
    }
    
    public Appointment(int id, LocalDateTime startTime){
        this.id = id;
        this.startTime = startTime;
    }
    
    public Appointment(String name, String start, String end, String phone, int id) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.phone = phone;
        this.id = id;
    }
    
    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, Timestamp start1, Timestamp end1, Timestamp createDate, String createdBy, String lastUpdateBy) {
        this.id = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start1 = start1;
        this.end1 = end1;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }

    
}
    
    

