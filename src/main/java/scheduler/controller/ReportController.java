package scheduler.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import scheduler.dao.AppointmentDAO;
import scheduler.dao.CustomerDAO;
import scheduler.helper.MonthCount;
import scheduler.helper.SceneHelper;
import scheduler.helper.SessionData;
import scheduler.helper.TypeCount;
import scheduler.model.Appointment;
import scheduler.model.Contact;
import scheduler.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for the Report View
 * @author Alvin Roe
 */

public class ReportController implements Initializable{
    //region Variables
    /**Reference to Combo Box. Used dynamically based on reportType (which is changed by switching radiobuttons)*/
    @FXML
    private ComboBox comboBoxOne;
    /**Reference for the Radio Button that changes the reportType to COUNT_APPOINTMENTS*/
    @FXML
    private RadioButton countAppointmentsRadio;
    /**Reference for the Radio Button that changes the reportType to CUSTOMER_APPOINTMENTS*/
    @FXML
    private RadioButton customerSchedule;
    /**Reference for the Radio Button that changes the reportType to CONTACT_SCHEDULE*/
    @FXML
    private RadioButton contactScheduleRadioButton;
    /**Reference for the Table View to display the report data*/
    @FXML
    private TableView reportTableView;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnOne;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnTwo;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnThree;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnFour;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnFive;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnSix;
    /**Reference for table column to display report data*/
    @FXML
    private TableColumn columnSeven;
    /**List of all appointments in database, initialized in the Initialize method*/
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    /**Each enum state represents a different Radio Button/Report*/
    private enum ReportType {NONE, COUNT_APPOINTMENTS, CONTACT_SCHEDULE, CUSTOMER_APPOINTMENTS}
    private ReportType reportType = ReportType.NONE;
    /**Common date time formatter used*/
    private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a");
    //endregion

    /**
     * Triggered when the radio button is changed to Count Appointments. Sets up combo box with correct data.
     */
    @FXML
    private void onCountAppointments(){
        toggleRadioButton(ReportType.COUNT_APPOINTMENTS);
        ObservableList<String> comboBoxItems = FXCollections.observableArrayList();
        comboBoxItems.add("Type");
        comboBoxItems.add("Month");
        setComboBox(comboBoxOne, comboBoxItems, "Count by...");
    }
    /**
     * Triggered when the radio button is changed to Contact Schedule. Sets up combo box with correct data.
     */
    @FXML
    private void onContactRadioPressed(){
        toggleRadioButton(ReportType.CONTACT_SCHEDULE);

        ObservableList<Contact> contacts = SessionData.getContacts();
        setComboBox(comboBoxOne, contacts, "Choose Contact");
    }
    /**
     * Triggered when the radio button is changed to Customer Schedule. Sets up combo box with correct data.
     */
    @FXML
    private void onCustomerSchedule() throws SQLException {
        toggleRadioButton(ReportType.CUSTOMER_APPOINTMENTS);
        setComboBox(comboBoxOne, CustomerDAO.selectAllCustomers(), "Choose a Customer");

        columnOne.setText("Appointment ID");
        columnTwo.setText("Start");
        columnThree.setText("End");
        columnFour.setText("Contact ID");
    }

    /**
     * Triggered whenever the combo box is updated in the view. Will fill in different data into the TableView
     * based on the reportType
     * @throws SQLException
     */
    @FXML
    private void onComboBoxOne() throws SQLException {
        if(comboBoxOne.getSelectionModel().getSelectedItem() == null) return;

        switch(reportType){
            case COUNT_APPOINTMENTS:
                countCustomers(appointments);
                break;
            case CONTACT_SCHEDULE:
                contactScheduleReport();
                break;
            case CUSTOMER_APPOINTMENTS:
                processCustomerAppointmentReport();
                break;
            default:
                break;
        }
    }

    /**
     * Triggered when the Schedule button is pressed. Takes user back to the Schedule View
     * @throws IOException
     */
    @FXML
    private void onSchedulePressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.SCHEDULE, comboBoxOne,this);
    }

    /**
     * Handles which radio button is active, and clears data as needed when a button is pressed. Called by all of the
     * radio buttons.
     * @param reportType
     */
    private void toggleRadioButton(ReportType reportType){
        reportTableView.getItems().clear();
        for(int i = 0; i < reportTableView.getColumns().size(); i++){
            TableColumn column = (TableColumn) reportTableView.getColumns().get(i);
            column.setText("");
            column.setCellValueFactory(null);
        }

        switch(reportType){
            case COUNT_APPOINTMENTS:
                this.reportType = ReportType.COUNT_APPOINTMENTS;
                countAppointmentsRadio.setSelected(true);
                contactScheduleRadioButton.setSelected(false);
                customerSchedule.setSelected(false);
                break;
            case CONTACT_SCHEDULE:
                this.reportType = ReportType.CONTACT_SCHEDULE;
                contactScheduleRadioButton.setSelected(true);
                countAppointmentsRadio.setSelected(false);
                customerSchedule.setSelected(false);
                break;
            case CUSTOMER_APPOINTMENTS:
                this.reportType = ReportType.CUSTOMER_APPOINTMENTS;
                customerSchedule.setSelected(true);
                countAppointmentsRadio.setSelected(false);
                contactScheduleRadioButton.setSelected(false);
                break;
        }
    }

    /**
     * Helper method to change the contents of a combo box.
     *
     * Lambda justification: Each one is used the same thing; to shorten the code to customize Cell Value that goes into the table. In order to do the same thing,
     * with Instances, it requires 11 lines of code that are somewhat difficult to read. Lambdas shorten that to one line, and it is much easier to follow.
     *
     * @param combobox the combo box to update
     * @param items the list to populate the combo box with
     * @param prompt the new prompt text
     */
    private void setComboBox(ComboBox combobox, ObservableList items, String prompt){
        combobox.setDisable(false);
        combobox.setVisible(true);
        combobox.setItems(items);
        combobox.setPromptText(prompt);
    }

    /**
     * Displays information into the TableView based on whether the comboBox says "Month" or "Type"
     * Will display counts of customers by types or months
     * Lambda justification: Each one is used the same thing; to shorten the code to customize Cell Value that goes into the table. In order to do the same thing,
     * with Instances, it requires 11 lines of code that are somewhat difficult to read. Lambdas shorten that to one line, and it is much easier to follow.
     *
     * @param appointments
     */
    private void countCustomers(ObservableList<Appointment> appointments){
        ObservableList<TypeCount> typeCounts = FXCollections.observableArrayList();

            if(comboBoxOne.getSelectionModel().getSelectedItem().equals("Type")) {
                //Set comboBoxTwo up with the different types
                for(int i = 0; i < appointments.size(); i++){
                    String type = appointments.get(i).getType();
                    
                    if(typeCounts.size() == 0){
                        typeCounts.add(new TypeCount(type, 1));
                    }
                    
                    for(int j = 0; j < typeCounts.size(); j++){
                        if(typeCounts.get(j).getType().equals(type)) {
                            typeCounts.get(j).addToCount(1);
                            break;
                        }
                        else if(j == typeCounts.size() - 1) {
                            typeCounts.add(new TypeCount(type, 1));
                        }
                    }
                }

                reportTableView.setItems(typeCounts);
                columnOne.setText("Type");
                columnTwo.setText("Count");
                columnOne.setCellValueFactory(new PropertyValueFactory("type"));
                columnTwo.setCellValueFactory(new PropertyValueFactory("count"));

            }
            else if(comboBoxOne.getSelectionModel().getSelectedItem() == "Month") {
                ObservableList<MonthCount> monthCounts = FXCollections.observableArrayList();
                monthCounts.add(new MonthCount(Month.JANUARY, 0, "January"));
                monthCounts.add(new MonthCount(Month.FEBRUARY, 0, "February"));
                monthCounts.add(new MonthCount(Month.MARCH, 0, "March"));
                monthCounts.add(new MonthCount(Month.APRIL, 0, "April"));
                monthCounts.add(new MonthCount(Month.MAY, 0, "May"));
                monthCounts.add(new MonthCount(Month.JUNE, 0, "June"));
                monthCounts.add(new MonthCount(Month.JULY, 0, "July"));
                monthCounts.add(new MonthCount(Month.AUGUST, 0, "August"));
                monthCounts.add(new MonthCount(Month.SEPTEMBER, 0, "September"));
                monthCounts.add(new MonthCount(Month.OCTOBER, 0, "October"));
                monthCounts.add(new MonthCount(Month.NOVEMBER, 0, "November"));
                monthCounts.add(new MonthCount(Month.DECEMBER, 0, "December"));

                for(int i = 0; i < appointments.size(); i++){
                    Appointment appointment = appointments.get(i);
                    Month month = appointment.getStartDate().getMonth();

                    monthCounts.get(month.getValue() - 1).addToCount(1);
                }

                reportTableView.setItems(monthCounts);
                columnOne.setText("Month");
                columnTwo.setText("Count");
                columnOne.setCellValueFactory(new PropertyValueFactory("name"));
                columnTwo.setCellValueFactory(new PropertyValueFactory("count"));

            }
    }
    /**
     *Processes and displays contact schedules based on the selected contact in the combo box.
     *
     * Lambda justification: Each one is used the same thing; to shorten the code to customize Cell Value that goes into the table. In order to do the same thing,
     * with Instances, it requires 11 lines of code that are somewhat difficult to read. Lambdas shorten that to one line, and it is much easier to follow.
     *
     */
    private void contactScheduleReport(){
        int contactID = ((Contact)comboBoxOne.getSelectionModel().getSelectedItem()).getContactID();
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

        for(int i = 0; i < appointments.size(); i++){
            if(appointments.get(i).getContactID() == contactID) contactAppointments.add(appointments.get(i));
        }

        reportTableView.setItems(contactAppointments);
        columnOne.setText("Appointment ID");
        columnOne.setCellValueFactory(new PropertyValueFactory("appointmentID"));
        columnTwo.setText("Title");
        columnTwo.setCellValueFactory(new PropertyValueFactory("title"));
        columnThree.setText("Type");
        columnThree.setCellValueFactory(new PropertyValueFactory("type"));
        columnFour.setText("Description");
        columnFour.setCellValueFactory(new PropertyValueFactory("description"));
        columnFive.setText("Start");
        columnFive.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>) cellDataFeatures -> {
            Appointment appointment = cellDataFeatures.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a");
            return Bindings.createStringBinding(() -> appointment.getStartDate().format(formatter));
        });
        columnSix.setText("End");
        columnSix.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>) data -> Bindings.createStringBinding(
                () -> data.getValue().getEndDate().format(dateTimeFormat)));
        columnSeven.setText("Customer ID");
        columnSeven.setCellValueFactory(new PropertyValueFactory("customerID"));
    }

    /**
     * Processes and displays information for customer appointments based on the customer displayed in the combobox
     *
     * Lambda justification: Each one is used the same thing; to shorten the code to customize Cell Value that goes into the table. In order to do the same thing,
     * with Instances, it requires 11 lines of code that are somewhat difficult to read. Lambdas shorten that to one line, and it is much easier to follow.
     *
     * @throws SQLException
     */
    private void processCustomerAppointmentReport() throws SQLException {
        int selectedCustomerID = ((Customer)comboBoxOne.getSelectionModel().getSelectedItem()).getId();
        ObservableList<Appointment> customerAppointments = AppointmentDAO.selectAppointmentsByCustomerID(selectedCustomerID);

        reportTableView.setItems(customerAppointments);
        columnOne.setCellValueFactory(new PropertyValueFactory("appointmentID"));
        columnTwo.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>) data -> Bindings.createStringBinding(
                () -> data.getValue().getStartDate().format(dateTimeFormat)));
        columnThree.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>) data -> Bindings.createStringBinding(
                () -> data.getValue().getEndDate().format(dateTimeFormat)));
        columnFour.setCellValueFactory(new PropertyValueFactory("contactID"));

    }

    /**
     * Pulls the appointments from the database and populates it to a list.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointments = AppointmentDAO.selectAllAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
