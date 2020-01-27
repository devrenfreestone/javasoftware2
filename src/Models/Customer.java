/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dev
 */
public class Customer {
    int id;
    String name;
    String city;
    String country;
    String phone;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
    public static void addCustomer(Customer customer){
        allCustomers.add(customer);
    }
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public Customer(int id, String name, String city, String country, String phone) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.phone = phone;
    }
    
}
