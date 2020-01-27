/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Utils.Query;
import java.io.IOException;
import java.net.URL;
import Models.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class EditCustomerController implements Initializable {
    String q;
    String userName;
    LocalDateTime date = LocalDateTime.now();
    int search = -1;
        

    @FXML
    private TextField CustomerID;
    @FXML
    private TextField CustomerName;
    @FXML
    private TextField AddressOne;
    @FXML
    private TextField AddressTwo;
    @FXML
    private TextField City;
    @FXML
    private TextField Country;
    @FXML
    private TextField Zip;
    @FXML
    private TextField Phone;
    @FXML
    private Button New;
    @FXML
    private Button Save;
    @FXML
    private Button Cancel;
    @FXML
    private TextField SearchText;
    @FXML
    private Button SearchButton;
    @FXML
    private TableView<Customer> CustomerList;
    @FXML
    private Button Delete;
    @FXML
    private Button Close1;
    
    private ObservableList<Customer> customer = FXCollections.observableArrayList();
    private ObservableList<Customer> customerSearch = FXCollections.observableArrayList();
    private Customer newCust;

    EditCustomerController(String userName) {
        this.userName = userName;
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        generateCustomerTable();
        
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
        q = "SELECT * FROM customer";
        ResultSet result = Query.makeQuery(q);
        int idCounter = 0;
        while(result.next()){
//            System.out.println(result.getInt("customerId"));
            if(result.getInt("customerId") > idCounter){
                idCounter = result.getInt("customerId");
            }
        }
        idCounter += 1;
        CustomerID.setText(String.valueOf(idCounter));
        Clear();
    }
    
    private void Clear(){
        CustomerName.clear();
        AddressOne.clear();
        AddressTwo.clear();
        City.clear();
        Country.clear();
        Zip.clear();
        Phone.clear();
    }

    @FXML
    private void Save(MouseEvent event) throws SQLException {
        q = "SELECT * FROM customer";
        String q2;
        String validCities = "SELECT city FROM city where city = '" + City.getText().trim() + "'";
        String validCountries = "SELECT country FROM country where country = '" + Country.getText().trim() + "'";
        ResultSet result = Query.makeQuery(q);
        ResultSet cities = Query.makeQuery(validCities);
        ResultSet countries = Query.makeQuery(validCountries);
        int idCounter = 0;
        int cityCounter = 0;
        int countryCounter = 0;
        
        //begin validations
        if(CustomerName.getText().trim().length() == 0){
            alertMessage("Customer Name cannot be blank.");
            return;
        }
        if(AddressOne.getText().trim().length() == 0){
            alertMessage("AddressOne cannot be blank");
            return;
        }
        while(cities.next()){
            cityCounter +=1;
        }
        if(cityCounter != 1){
            alertMessage("Invalid city. Valid cities are: Phoenix, New York, London, or Mexico City.");
            return;
        }
        while(countries.next()){
            countryCounter +=1;
        }
        if(countryCounter != 1){
            alertMessage("Inavlid country. Valid countries are: USA, England, or Mexico.");
        }
        if(Zip.getText().trim().length() == 0){
            alertMessage("Postal Code cannot be blank.");
        }
        if(Phone.getText().trim().length() == 0){
            alertMessage("Phone cannot be blank.");
        }
        
        //begin update/insert
        while(result.next()){
            idCounter += 1;
            if( idCounter == 1){
              //update record  
              q2 = "update \n" +
                    "U05Hru.customer cu, U05Hru.address ad, U05Hru.city ci, U05Hru.country co\n" +
                    "set\n" +
                    "cu.customerName = '" + CustomerName.getText().trim() + "',\n" +
                    "ad.address = '" + AddressOne.getText().trim() + "',\n" +
                    "ad.address2 = '" + AddressTwo.getText().trim() + "',\n" +
                    "ci.city = '" + City.getText().trim() + "',\n" +
                    "co.country = '" + Country.getText().trim() + "',\n" +
                    "ad.postalCode = '" + Zip.getText().trim() + "',\n" +
                    "ad.phone = '" + Phone.getText().trim() + "'\n" +
                    "where\n" +
                    "cu.customerId = '" + CustomerID.getText().trim() + "' and\n" +
                    "cu.addressId = ad.addressId and\n" +
                    "ad.cityId = ci.cityId and\n" +
                    "ci.countryId = co.countryId";
              Query.makeQuery(q2);
      
            } else{
              //add new record get city id from city name(select city where city name, return city id), insert into address first, get last inserted id from address table - see example, then insert into customer table with new address id, select/insert select/insert 
              q2 = "select cityId from city where city  = '" + City.getText().trim() + "'";
              result = Query.makeQuery(q2);
              int cityId = -1;
              while(result.next()){
                cityId = result.getInt("cityId");
              }
              q2 = "insert into address (address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdateBy) values('" + AddressOne.getText() + "','" + AddressTwo.getText() + "','" + cityId + "','" + Zip.getText().trim() + "','" + Phone.getText().trim() + "','" + date + "','" + userName + "','" + userName + "')";
              Query.makeQuery(q2);
              int addressId = -1;
              result = Query.makeQuery("select * from address");
              while(result.next()){
                  if(result.getInt("addressId") > addressId){
                      addressId = result.getInt("addressId");
                  }
              }
              q2 = "insert into customer (customerName,addressId,active,createDate,createdBy,lastUpdateBy) values('" + CustomerName.getText().trim() + "','" + addressId + "',0,'" + date + "','" + userName + "','" + userName + "')"; 
              Query.makeQuery(q2);
            }
        }
        CustomerID.clear();
        Clear();
        generateCustomerTable();
    }
    
    @FXML
    private void Select(MouseEvent event) throws SQLException {
        newCust = CustomerList.getSelectionModel().getSelectedItem();
        q = "select\n" +
            "cu.customerId Id,\n" +
            "cu.customerName Name,\n" +
            "ad.address Add1,\n" +
            "ad.address2 Add2,\n" +
            "ad.postalCode Zip,\n" +
            "ad.phone Phone,\n" +                
            "ci.city City,\n" +
            "co.country Country\n" +
            "\n" +
            "from\n" +
            "U05Hru.customer cu join\n" +
            "U05Hru.address ad on cu.addressId = ad.addressId join\n" +
            "U05Hru.city ci on ad.cityId = ci.cityId join\n" +
            "U05Hru.country co on ci.countryId = co.countryId\n" +
            "\n" +
            "where cu.customerId = '" + newCust.getId() + "'"  ;
        ResultSet result = Query.makeQuery(q);
        try{
            while(result.next()){
                CustomerID.setText(String.valueOf(result.getInt("Id")));
                CustomerName.setText(result.getString("Name"));
                AddressOne.setText(result.getString("Add1"));
                AddressTwo.setText(result.getString("Add2"));
                City.setText(result.getString("City"));
                Country.setText(result.getString("Country"));
                Zip.setText(result.getString("Zip"));
                Phone.setText(result.getString("Phone"));  
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Cancel(MouseEvent event) {
        CustomerID.clear();
        Clear();
    }

    @FXML
    private void Delete(MouseEvent event) {
        newCust = CustomerList.getSelectionModel().getSelectedItem();
        q = "DELETE from customer WHERE customerId = '" + newCust.getId() + "'";        
        Query.makeQuery(q);
        CustomerID.clear();
        Clear();
        generateCustomerTable();
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
    
    @FXML
    private void Search(MouseEvent event) throws IOException {
        if (search < 0){
            customerSearch.clear();
            for(Customer c: customer){
                if (c.getName().toLowerCase().contains(SearchText.getText().trim().toLowerCase())){
                    customerSearch.add(c);
                }
            }
            CustomerList.setItems(customerSearch);
            CustomerList.refresh();
            SearchButton.setText("Cancel");
        } else {
            customerSearch.clear();
            SearchButton.setText("Search");
            SearchText.clear();
            CustomerList.setItems(customer);
            CustomerList.refresh();
        }
        search = search * -1;
        System.out.println("New search value: " + search);
        
    }
    
    private void alertMessage(String field){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Invalid Customer Data");
           alert.setContentText(field);
           alert.showAndWait();
        }
    
}
