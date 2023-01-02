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
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controller for the Add Customer view
 * @author Alvin Roe
 */
public class AddCustomerController implements Initializable {
    //region FXML Variables
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
    //endregion
    //region Other Variables
    /**A list that holds different lists of First Level Divisions, separated by their respective countries*/
    private ObservableList<ObservableList<FirstLevelDivision>> allDivisions = FXCollections.observableArrayList();
    /**The method validateData will not run if this is false. This is set true after save is pressed the first time.*/
    private boolean validationEnabled = false;
    /**This is the Customer that will eventually be added into the database. It is updated in validateData, which is called by the controls and the save button*/
    Customer newCustomer = new Customer();
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
    private void onSavePressed() throws IOException, SQLException {
        validationEnabled = true;
        if(!validateData()) return;

        CustomerDAO.insert(newCustomer);

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

        //Customer ID
        newCustomer.setID(CustomerDAO.generateNewID());

        //Customer Name
        if(name.getText().length() == 0)
            errorNodes.add(name);
        else {
            newCustomer.setName(name.getText());
            errorFreeNodes.add(name);
        }

        //Customer Address
        if(address.getText().length() == 0)
            errorNodes.add(address);
        else {
            newCustomer.setAddress(address.getText());
            errorFreeNodes.add(address);
        }

        //Customer Postal Code
        if(postal.getText().length() == 0)
            errorNodes.add(postal);
        else {
            newCustomer.setPostal(postal.getText());
            errorFreeNodes.add(postal);
        }

        //Customer Phone Number
        if(phone.getText().length() == 0)
            errorNodes.add(phone);
        else {
            newCustomer.setPhone(phone.getText());
            errorFreeNodes.add(phone);
        }

        //Date this was created
        newCustomer.setCreateDate(ZonedDateTime.now());

        //User who created this
        newCustomer.setAuthor(UserDAO.currentUserName());

        //The last date that this was updated on
        newCustomer.setLastUpdate(ZonedDateTime.now());

        //User that was last logged in that last updated this
        newCustomer.setLastAuthor(UserDAO.currentUserName());

        //Division ID
        try{
            int divisionID = ((FirstLevelDivision)division.getSelectionModel().getSelectedItem()).getDivisionID();
            newCustomer.setDivisionID(divisionID);
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
        //Set Country Combo Box
        ObservableList<Country> countries = SessionData.getAllCountries();
        country.setItems(countries);
        country.getSelectionModel().selectFirst();

        //Set First Level Division Combo Box
        allDivisions = SessionData.getSeparatedFirstLevelDivisions();
        division.setItems(getSelectedCountryDivisions());
    }

    /**
     * Returns a list of all of the First Level Divisions for the Country currently selected in the combo box.
     * @return a list of all of the First Level Divisions for the Country currently selected in the combo box.
     */
    private ObservableList<FirstLevelDivision> getSelectedCountryDivisions(){
        //IDs start at 1, whereas index starts at 0, so 1 needs to be subtracted to get the index
        int selectedCountryID = ((Country)country.getSelectionModel().getSelectedItem()).getCountryID();
        for(int i = 0; i < allDivisions.size(); i++){
            if(allDivisions.get(i).get(0).getCountryID() == selectedCountryID)  return allDivisions.get(i);
        }
        return FXCollections.observableArrayList();
    }

    /**
     * Updates the First Level Division combo box whenever the Country combo box is changed.
     * @throws SQLException
     */
    @FXML
    private void onCountryChange() throws SQLException {
        division.setItems(getSelectedCountryDivisions());
        division.getSelectionModel().selectFirst();
        validateData();
    }

}
