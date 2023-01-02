package scheduler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import scheduler.dao.CustomerDAO;
import scheduler.dao.UserDAO;
import scheduler.helper.SceneHelper;
import scheduler.helper.SessionData;
import scheduler.model.Country;
import scheduler.model.Customer;
import scheduler.model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/**
 * Controller for the Update Customer View
 *
 * @author Alvin Roe
 */

public class UpdateCustomerController implements Initializable {

    //region Variables
    /**Field for name input in Add Customer view*/
    @FXML
    private TextField name;
    /**Text field for Address*/
    @FXML
    private TextField address;
    /**Combo Box to select First Level Division, ie. State if US or Province if Canada*/
    @FXML
    private ComboBox division;
    /**Combo Box to select the country*/
    @FXML
    private ComboBox country;
    /**Text field for postal code*/
    @FXML
    private TextField postal;
    /**Text field for phone number*/
    @FXML
    private TextField phone;
    /**Reference to the TextField for ID*/
    @FXML
    private TextField id;
    /**A list that holds different lists of First Level Divisions, separated by their respective countries*/
    private ObservableList<ObservableList<FirstLevelDivision>> separatedDivisions = FXCollections.observableArrayList();
    /**A list that holds all First level Divisions, separated by their respective countries*/
    private ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();
    /**The method validateData will not run if this is false. This is set true after save is pressed the first time.*/
    private boolean validationEnabled = false;
    /**Reference to the Country ID that is associated with this Customer*/
    private int countryID = 0;
    /**The customer data that will eventually be updated in the system*/
    Customer selectedCustomer = new Customer();
    //endregion
    /**
     * Handles logic for when the cancel button is pressed in the Add Customer view. Generally, just sends the user back to the Customer form.
     * @throws IOException
     */
    @FXML
    private void onCancelPressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.CUSTOMER, name, this);
    }
    /**
     * Takes the data from the form, validates it, then inserts it into the database. Enables validation the first time it runs, and will not
     * save anything if validateData returns false.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void onSavePressed() throws IOException, ParseException, SQLException {
        validationEnabled = true;
        if(!validateData()) return;

        CustomerDAO.update(selectedCustomer);

        SceneHelper.changeScene(SceneHelper.View.CUSTOMER, name, this);
    }
    /**
     * Checks through the form to make sure each field is filled out correctly. It will set borders to red of fields that are causing issues, it will
     * also return borders to normal if the fields are fixed.
     * @return Returns true if everything was validated and false if there were errors.
     * @throws SQLException
     */
    @FXML
    private boolean validateData() throws SQLException {
        if(!validationEnabled) return false; //Validation is only enabled after save is pressed for the first time, so that users are not being bombarded by changing fx when they did nothing wrong

        ObservableList<Node> errorNodes = FXCollections.observableArrayList();
        ObservableList<Node> errorFreeNodes = FXCollections.observableArrayList();

        //Customer Name
        if(name.getText().length() == 0)
            errorNodes.add(name);
        else {
            selectedCustomer.setName(name.getText());
            errorFreeNodes.add(name);
        }

        //Customer Address
        if(address.getText().length() == 0)
            errorNodes.add(address);
        else {
            selectedCustomer.setAddress(address.getText());
            errorFreeNodes.add(address);
        }

        //Customer Postal Code
        if(postal.getText().length() == 0)
            errorNodes.add(postal);
        else {
            selectedCustomer.setPostal(postal.getText());
            errorFreeNodes.add(postal);
        }

        //Customer Phone Number
        if(phone.getText().length() == 0)
            errorNodes.add(phone);
        else {
            selectedCustomer.setPhone(phone.getText());
            errorFreeNodes.add(phone);
        }

        //The last date that this was updated on
        selectedCustomer.setLastUpdate(ZonedDateTime.now());

        //User that was last logged in that last updated this
        selectedCustomer.setLastAuthor(UserDAO.currentUserName());

        //Division ID
        try{
            int divisionID = ((FirstLevelDivision)division.getSelectionModel().getSelectedItem()).getDivisionID();
            selectedCustomer.setDivisionID(divisionID);
            errorFreeNodes.add(division);
        } catch(NullPointerException e) {
            errorNodes.add(division);
        }

        //Resets style sheets for everything on the error free list
        for(int i = 0; i < errorFreeNodes.size(); i++) errorFreeNodes.get(i).setStyle("");

        //Checks for errors, if there are any, then it changes the border to red and returns false for the validation
        for (int i = 0; i < errorNodes.size(); i++) {
            errorNodes.get(i).setStyle("-fx-border-color: red");
            if(i == errorNodes.size() - 1)
                return false;
        }

        return true;
    }
    /**
     * Sets up the Combo Boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Customer> allCustomers = CustomerDAO.selectAllCustomers();
            for(int i = 0; i < allCustomers.size(); i++){
                if(allCustomers.get(i).getId() == SessionData.getModifyID()) selectedCustomer = allCustomers.get(i);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Country> countries = SessionData.getAllCountries();
        country.setItems(countries);
        countryID = SessionData.getCountryID(selectedCustomer.getDivisionID());
        country.getSelectionModel().select(countryID - 1);


        //Set First Level Division Combo Box
        separatedDivisions = SessionData.getSeparatedFirstLevelDivisions();
        allDivisions = SessionData.getAllFirstLevelDivisions();
        updateDivisionBox();
        int divisionIndex = 0;
        for(int i = 0; i < allDivisions.size(); i++){
            if(allDivisions.get(i).getDivisionID() == selectedCustomer.getDivisionID()) divisionIndex = i;
        }
        division.setValue(allDivisions.get(divisionIndex));


        //Fill in data
        id.setText(String.valueOf(selectedCustomer.getId()));
        name.setText(selectedCustomer.getName());
        address.setText(selectedCustomer.getAddress());
        postal.setText(selectedCustomer.getPostal());
        phone.setText(selectedCustomer.getPhone());


    }
    /**
     * Updates the First Level Division combo box whenever the Country combo box is changed.
     * @throws SQLException
     */
    @FXML
    private void onCountryChange() throws SQLException {
        countryID = ((Country)country.getSelectionModel().getSelectedItem()).getCountryID();
        updateDivisionBox();
        validateData();
    }
    /**
     * Updates the First Level Division combo box
     */
    private void updateDivisionBox(){
        division.setItems(separatedDivisions.get(countryID - 1));
        division.getSelectionModel().selectFirst();
    }
}
