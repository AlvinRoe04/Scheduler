package scheduler.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduler.dao.AppointmentDAO;
import scheduler.dao.CustomerDAO;
import scheduler.helper.AlertHelper;
import scheduler.helper.SceneHelper;
import scheduler.helper.SessionData;
import scheduler.model.Appointment;
import scheduler.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the Customer Form view. A general table showing all customers. Also, can take user to Add Customer view or Update Customer view.
 * It can also allow for users to delete customers.
 * @author Alvin Roe
 */
public class CustomerController implements Initializable {
    /**Table where the customers information is listed*/
    @FXML
    private TableView customerTable;
    /**Table Column for displaying Customer ID from the Customer Class*/
    @FXML
    private TableColumn id;
    /**Table Column for displaying Customer ID from the Customer Class*/
    @FXML
    private TableColumn name;
    /**Table Column for displaying Customer ID from the Customer Class*/
    @FXML
    private TableColumn address;
    /**Table Column for displaying Customer ID from the Customer Class*/
    @FXML
    private TableColumn postal;
    /**Table Column for displaying Customer ID from the Customer Class*/
    @FXML
    private TableColumn phone;
    /**Table Column for displaying Customer ID from the Customer Class*/
    @FXML
    private TableColumn division;
    /**Table Column for displaying Customer ID from the Customer Class*/

    /**
     * Takes user to the Add Customer view.
     * @throws IOException
     */
    @FXML
    private void onAddPressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.ADD_CUSTOMER, customerTable, this);

    }
    /**
     * Takes user to the Update Customer view. Also, handles needed logic for passing information for which Customer was selected when the Update button was pressed.
     * @throws IOException
     */
    @FXML
    private void onUpdatePressed() throws IOException {
        if(customerTable.getSelectionModel().getSelectedItem() == null) return;

        int customerID = ((Customer)customerTable.getSelectionModel().getSelectedItem()).getId();
        SessionData.setModifyID(customerID);

        SceneHelper.changeScene(SceneHelper.View.UPDATE_CUSTOMER, customerTable, this);
    }
    /**
     * Takes user back to the Schedule view.
     * @throws IOException
     */
    @FXML
    private void onSchedulePressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.SCHEDULE, customerTable, this);
    }
    /**
     * Deletes the selected customer if there are no appointments assigned to them. Gives an error if there are appointments assigned to them.
     * @throws SQLException
     */
    @FXML
    private void onDeletePressed() throws SQLException {
        Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();

        if(customer == null){
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error: Nothing Selected", "Nothing Selected", "Please select a customer to delete");
            return;
        }
        ObservableList<Appointment> customersAppointments = AppointmentDAO.selectAppointmentsByCustomerID(customer.getId());

        if(customersAppointments.size() > 0){
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error: Has Appointments", "Customer has Appointments", "Please delete customer's appointments first");
            return;
        }
        int customerID = customer.getId();

        CustomerDAO.delete(customerID);
        customerTable.getItems().remove(customer);
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Delete Successful", "Customer# " + customerID + " Deleted", "Deleted Successfully");
    }
    /**
     * Runs before everything else when the view is loaded. Sets up the tableview.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerTable.setItems(CustomerDAO.selectAllCustomers());
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postal.setCellValueFactory(new PropertyValueFactory<>("postal"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            division.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
