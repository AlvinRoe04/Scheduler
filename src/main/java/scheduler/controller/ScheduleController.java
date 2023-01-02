package scheduler.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import scheduler.dao.AppointmentDAO;
import scheduler.helper.AlertHelper;
import scheduler.helper.SceneHelper;
import scheduler.helper.SessionData;
import scheduler.model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * Controller for the Schedule View.
 * @author Alvin Roe
 */
public class ScheduleController implements Initializable {
    //region FXML Variables
    /**Table where all the customers from the database are loaded*/
    @FXML
    private TableView appointmentTable;
    /**Table Column to display the Appointment ID for Appointments*/
    @FXML
    private TableColumn appointmentID;
    /**Table Column to display the title for Appointments*/
    @FXML
    private TableColumn title;
    /**Table Column to display the description for Appointments*/
    @FXML
    private TableColumn description;
    /**Table Column to display the location for Appointments*/
    @FXML
    private TableColumn location;
    /**Table Column to display the contact for Appointments*/
    @FXML
    private TableColumn contact;
    /**Table Column to display the type for Appointments*/
    @FXML
    private TableColumn type;
    /**Table Column to display the Start Date for Appointments*/
    @FXML
    private TableColumn startDate;
    /**Table Column to display the Start Time for Appointments*/
    @FXML
    private TableColumn startTime;
    /**Table Column to display the End Date for Appointments*/
    @FXML
    private TableColumn endDate;
    /**Table Column to display the End Time for Appointments*/
    @FXML
    private TableColumn endTime;
    /**Table Column to display the Customer ID for Appointments*/
    @FXML
    private TableColumn customerID;
    /**Table Column to display the User ID for Appointments*/
    @FXML
    private TableColumn userID;
    /**Radio Button for Viewing All Appointments*/
    @FXML
    private RadioButton viewAll;
    /**Radio Button for Viewing Appointments within the next 7 days*/
    @FXML
    private RadioButton viewWeek;
    /**Radio Button for Viewing Appointments for the current month*/
    @FXML
    private RadioButton viewMonth;
    //endregion
    //region Other Variables
    /**Each enum state is a different radio button*/
    private enum TimeSpanSelection {all, week, month}
    private TimeSpanSelection radioButtonSelected;
    /**A list for all of the appointments*/
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    /**A list for the current Month's appointments*/
    private ObservableList<Appointment> currentMonthAppointments = FXCollections.observableArrayList();
    /**A list for the appointments for the next 7 days*/
    private ObservableList<Appointment> currentWeekAppointments = FXCollections.observableArrayList();
    /**If this is false SessionData will have it's initialize data method ran once, then it will be set to true. This is within the Intialize method*/
    private static boolean initialized = false;
    //endregion
    /**
     * Changes scene to the customer form view when the "Customer" button is pressed
     * @throws IOException
     */
    @FXML
    private void onCustomerPressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.CUSTOMER, appointmentTable, this);
    }
    /**
     * Changes to the Add Customer view when the add button is pressed
     * @throws IOException
     */
    @FXML
    private void onAddPressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.ADD_APPOINTMENT, appointmentTable, this);
    }
    /**
     *  Changes to the Update Appointment view when the Update button is pressed. Will display error if nothing is selected.
     *  Sets the "modifyID" in the SessionData which is used in the UpdateView to know which appointment to update.
     * @throws IOException
     */
    @FXML
    private void onUpdatePressed() throws IOException {
        try {
            int appointmentID = ((Appointment) appointmentTable.getSelectionModel().getSelectedItem()).getAppointmentID();
            SessionData.setModifyID(appointmentID);
            SceneHelper.changeScene(SceneHelper.View.UPDATE_APPOINTMENT, appointmentTable, this);
        }catch(NullPointerException error){
            Alert nothingSelected = new Alert(Alert.AlertType.ERROR);
            nothingSelected.setTitle("Nothing Selected");
            nothingSelected.setContentText("Please select an appointment to update");
            nothingSelected.showAndWait();
        }
    }
    /**
     * Deletes the selected Appointment
     * @throws SQLException
     */
    @FXML
    private void onDeletePressed() throws SQLException {
        Appointment selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null){
            Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle("Nothing Selected");
            nothingSelectedAlert.setHeaderText("Nothing Selected");
            nothingSelectedAlert.setContentText("Please select an Appointment to Delete");
            nothingSelectedAlert.showAndWait();
            return;
        }

        Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirm.setTitle("Confirm Delete");
        deleteConfirm.setHeaderText("Warning!");
        deleteConfirm.setContentText("This will permanently delete this file.");
        Optional<ButtonType> option = deleteConfirm.showAndWait();

        if(option.get() == ButtonType.OK) {
            int id = selectedAppointment.getAppointmentID();
            String type = selectedAppointment.getType();
            AppointmentDAO.delete(selectedAppointment.getAppointmentID());
            appointmentTable.getItems().remove(selectedAppointment);

            Alert notificationDelete = new Alert(Alert.AlertType.INFORMATION);
            notificationDelete.setTitle("Delete Successful");
            notificationDelete.setHeaderText("Appointment ID# " + id + ": " + type);
            notificationDelete.setContentText("Successfully Deleted");
            notificationDelete.show();
        }
    }

    /**
     * Initializes all of the SessionData. Populates TableView.
     *
     * Lambda justification: Each one is used the same thing; to shorten the code to customize Cell Value that goes into the table. In order to do the same thing,
     * with Instances, it requires 11 lines of code that are somewhat difficult to read. Lambdas shorten that to one line, and it is much easier to follow.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Runs SessionData to grab data that is not going to change until there is a logout

        if(!initialized) {
            try {
                SessionData.InitializeSessionData();
                initialized = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        //Fill the table with the schedule data.
        try {
            //Set allAppointments to the appointmentTable
            allAppointments = AppointmentDAO.selectAllAppointments();
            appointmentTable.setItems(allAppointments);
            appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
            location.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            contact.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>) data -> Bindings.createStringBinding(
                    () -> SessionData.getContactName(data.getValue().getContactID())));
            type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            startDate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>)
                    data -> Bindings.createStringBinding(() -> data.getValue().getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"))));
            startTime.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>)
                    data -> Bindings.createStringBinding(() -> data.getValue().getStartDate().format(DateTimeFormatter.ofPattern("hh:mm a"))));
            endDate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>)
                    data -> Bindings.createStringBinding(() -> data.getValue().getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"))));
            endTime.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>)
                    data -> Bindings.createStringBinding(() -> data.getValue().getEndDate().format(DateTimeFormatter.ofPattern("hh:mm a"))));
            customerID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerID"));
            userID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userID"));

            /*Checks through all the appointments, created a list of all of them that happen within 7 days, another list with all of them that happen this month, and
            checks through appointments for one that is happening withing the next fifteen minutes */
            boolean noAppointments = true;
            for(Appointment appointment : allAppointments) {
                LocalDate appointmentDate = appointment.getStartDate().toLocalDate();
                LocalTime fifteenMinuteTime = LocalTime.now().plusMinutes(16);
                if ((appointmentDate.isAfter(LocalDate.now()) || appointmentDate.isEqual(LocalDate.now())) && appointmentDate.isBefore(LocalDate.now().plusDays(8)))
                    currentWeekAppointments.add(appointment);
                if (appointmentDate.getMonth() == LocalDate.now().getMonth()) currentMonthAppointments.add(appointment);
                if(appointment.getStartDate().toLocalTime().isBefore(fifteenMinuteTime) && appointmentDate.isEqual(LocalDate.now())
                && appointment.getStartDate().toLocalTime().isAfter(LocalTime.now().minusMinutes(1))) {
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, "15 Minute Warning", "Appointment# " + appointment.getAppointmentID(),
                            appointment.getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:mm a")));
                    noAppointments = false;
                }
            }
            if(noAppointments) AlertHelper.showAlert(Alert.AlertType.INFORMATION, "No Appointments", "No Appointments", "Your schedule is clear for the next 15 minutes");




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Handles logic for when the View All Radio button is pressed
     */
    @FXML
    private void onViewAll(){
        radioButtonSelected = TimeSpanSelection.all;
        toggleRadioButton(radioButtonSelected);

        appointmentTable.setItems(allAppointments);
    }
    /**
     * Handles logic for when the View Week button is pressed
     */
    @FXML
    private void onViewWeek(){
        radioButtonSelected = TimeSpanSelection.week;
        toggleRadioButton(radioButtonSelected);

        appointmentTable.setItems(currentWeekAppointments);
    }
    /**
     * Handles logic for when the View Month button is pressed
     */
    @FXML
    private void onViewMonth(){
        radioButtonSelected = TimeSpanSelection.month;
        toggleRadioButton(radioButtonSelected);

        appointmentTable.setItems(currentMonthAppointments);
    }
    /**
     * Helper method that changes which Radio Button is selected
     */
    private void toggleRadioButton(TimeSpanSelection radioButtonSelected){
        switch(radioButtonSelected){
            case all:
                viewAll.setSelected(true);
                viewWeek.setSelected(false);
                viewMonth.setSelected(false);
                break;
            case week:
                viewWeek.setSelected(true);
                viewAll.setSelected(false);
                viewMonth.setSelected(false);
                break;
            case month:
                viewMonth.setSelected(true);
                viewAll.setSelected(false);
                viewWeek.setSelected(false);
                break;
        }
    }
    /**
     * Changes to the Reports View when the Report button is pressed
     * @throws IOException
     */
    @FXML
    private void onReportsPressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.REPORT, appointmentTable, this);
    }
}


