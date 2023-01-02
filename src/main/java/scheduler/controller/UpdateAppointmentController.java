package scheduler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import scheduler.dao.CustomerDAO;
import scheduler.helper.*;
import scheduler.model.*;
import javafx.scene.text.Text;
import scheduler.dao.AppointmentDAO;



import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ResourceBundle;
import java.time.*;

/**
 * Controller for the Add Appointment view
 * @author Alvin Roe
 */
public class UpdateAppointmentController implements Initializable {
    //region FXML Variables
    /**Reference to the field labeled "Title" in the Add Apointment view*/
    @FXML
    private TextField titleField;
    /**Reference to Text Field for location*/
    @FXML
    private TextField locationField;
    /**Reference to Text Field for type*/
    @FXML
    private TextField typeField;
    /**Reference to Text Field for contact ID*/
    @FXML
    private ComboBox<Contact> contactIDBox;
    /**Reference to Text Field for customer ID*/
    @FXML
    private ComboBox<Customer> customerIDBox;
    /**Reference to Date Picker for the Start Date*/
    @FXML
    private DatePicker startDatePicker;
    /**Reference to Date Picker for the End Date*/
    @FXML
    private DatePicker endDatePicker;
    /**Reference to Text Field for the Start Hour*/
    @FXML
    private TextField startHourField;
    /**Reference to Text Field for the Start Minute*/
    @FXML
    private TextField startMinuteField;
    /**Reference to Combo Box to change between AM/PM for Start Time*/
    @FXML
    private ComboBox<MorningAfternoon> startTODCombo;
    /**Reference to Combo Box to change between AM/PM for End Time*/
    @FXML
    private ComboBox<MorningAfternoon> endTODCombo;
    /**Reference to Text Field for the End Hour*/
    @FXML
    private TextField endHourField;
    /**Reference to Text Field for the End Minute*/
    @FXML
    private TextField endMinuteField;
    /**Reference to Text Field for the Description*/
    @FXML
    private TextField descriptionField;
    /**Reference to Text Field for the Displaying context errors next to Title Text Field*/
    @FXML
    private Text titleErrorText;
    /**Reference to Text Field for the Displaying context errors next to Location Text Field*/
    @FXML
    private Text locationErrorText;
    /**Reference to Text Field for the Displaying context errors next to Type Text Field*/
    @FXML
    private Text typeErrorText;
    /**Reference to Text Field for the Displaying context errors next to Description Text Field*/
    @FXML
    private Text descriptionErrorText;
    /**Reference to the Text Field for ID.*/
    @FXML
    private TextField idField;

    //endregion
    //region Other Variables
    /**Used to turn on dyanamicValidateDate(), so that errors do not start showing up until save is pressed. This is for user experience*/
    private boolean validatingData = false;
    /**The Appointment that will eventually be added to the database. It is added by pressing the "save" button, which triggers onSavePressed()*/
    private Appointment modifyAppointment = new Appointment();
    //endregion
    //region On Button Pressed Methods
    /**
     * Handles logic for when the cancel button is pressed in the Add Appointment View. Typically just changes the scene to the Schedule view.
     * @throws IOException
     */
    @FXML
    private void onCancelPressed() throws IOException {
        SceneHelper.changeScene(SceneHelper.View.SCHEDULE, titleField, this);
    }
    /**
     * Validates data in fields. If data is valid, saves data to newAppointment, and adds it to the Database, then changes scene back to the complete Schedule
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    private void onSavePressed() throws SQLException, IOException {
        if(!validatingData) validatingData = true;
        if(!validateData()) return;

        AppointmentDAO.update(modifyAppointment);
        SceneHelper.changeScene(SceneHelper.View.SCHEDULE, titleField, this);
    }
    //endregion
    //region Data Validation
    /**
     * Checks for errors in the selections and highlights boxes accordingly. Will always return false
     * if validatingData is not true. ValidatingData becomes true when the Save Button is pressed for the first time.
     * Called by validateData.
     * @return true if the data is valid. If false, the data is invalid and doesn't meet standards.
     * @throws SQLException
     */
    private boolean dynamicValidateData() throws SQLException {
        if(!validatingData) return false;
        ObservableList<Node> allErrorControls = FXCollections.observableArrayList();
        ObservableList<Node> errorFreeControls = FXCollections.observableArrayList();

        //validateData calls dyanamicValidateData before shortening Strings. H
        //Title
        if(titleField.getText().length() > 0) {
            modifyAppointment.setTitle(titleField.getText());
            errorFreeControls.add(titleField);
        }
        else allErrorControls.add(titleField);

        //Description
        if(descriptionField.getText().length() > 0) {
            modifyAppointment.setDescription(descriptionField.getText());
            errorFreeControls.add(descriptionField);
        }
        else allErrorControls.add(descriptionField);

        //Location
        if(locationField.getText().length() > 0) {
            modifyAppointment.setLocation(locationField.getText());
            errorFreeControls.add(locationField);
        }
        else allErrorControls.add(locationField);

        //Type
        if(typeField.getText().length() > 0) {
            modifyAppointment.setType(typeField.getText());
            errorFreeControls.add(typeField);
        }
        else allErrorControls.add(typeField);

        //Customer ID
        try {
            int customerID = (customerIDBox.getSelectionModel().getSelectedItem()).getId();
            modifyAppointment.setCustomerID(customerID);
        }catch(NullPointerException error) { allErrorControls.add(customerIDBox); }

        //Start Date and End Date
        ZonedDateTime startDate;
        ZonedDateTime endDate;
        startDate = validateDateTime(startDatePicker, startHourField, startMinuteField, startTODCombo, allErrorControls, errorFreeControls);
        endDate = validateDateTime(endDatePicker, endHourField, endMinuteField, endTODCombo, allErrorControls, errorFreeControls);

        //Get Long Versions of the Dates
        long startDateNumber = 0;
        long endDateNumber = 0;

        if(startDate != null && endDate != null){
            startDateNumber = startDate.getLong(ChronoField.INSTANT_SECONDS);
            endDateNumber = endDate.getLong(ChronoField.INSTANT_SECONDS);

            //Check if startDate < endDate. If so this is good to go.
            if(startDateNumber < endDateNumber){
                modifyAppointment.setStartDate(startDate);
                modifyAppointment.setEndDate(endDate);
            }
            else if(startDate.getDayOfYear() == endDate.getDayOfYear()){
                allErrorControls.add(startHourField);
                allErrorControls.add(startMinuteField);
                allErrorControls.add(endHourField);
                allErrorControls.add(endMinuteField);
            }
            else{
                allErrorControls.add(startDatePicker);
                allErrorControls.add(endDatePicker);
            }

        }

        ObservableList<Appointment> appointmentsWithCustomer = AppointmentDAO.selectAppointmentsByCustomerID(modifyAppointment.getCustomerID());
        for(int i = 0; i < appointmentsWithCustomer.size(); i++){
            if(startDateNumber == 0 || endDateNumber == 0) break;
            if(appointmentsWithCustomer.get(i).getAppointmentID() == modifyAppointment.getAppointmentID()) continue;

            long loopAppointmentStart = appointmentsWithCustomer.get(i).getStartDate().getLong(ChronoField.INSTANT_SECONDS);
            long loopAppointmentEnd = appointmentsWithCustomer.get(i).getEndDate().getLong(ChronoField.INSTANT_SECONDS);

            if((startDateNumber >= loopAppointmentStart && startDateNumber < loopAppointmentEnd) || (endDateNumber >= loopAppointmentStart && endDateNumber <= loopAppointmentEnd)
                    || (startDateNumber <= loopAppointmentStart && endDateNumber >= loopAppointmentEnd)){
                Alert overlapAlert = new Alert(Alert.AlertType.WARNING);
                overlapAlert.setTitle("Overlapping Appointment");
                String startDateString = appointmentsWithCustomer.get(i).getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                String startTime = appointmentsWithCustomer.get(i).getStartDate().format(DateTimeFormatter.ofPattern("hh:mm a"));
                String endDateString = appointmentsWithCustomer.get(i).getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                String endTime = appointmentsWithCustomer.get(i).getEndDate().format(DateTimeFormatter.ofPattern("hh:mm a"));
                overlapAlert.setContentText("Overlaps with Appointment ID# " + appointmentsWithCustomer.get(i).getAppointmentID() + " which is scheduled " +
                        startDateString + " " + startTime + " to " + endDateString + " " + endTime);
                overlapAlert.showAndWait();

                allErrorControls.add(startDatePicker);
                allErrorControls.add(startHourField);
                allErrorControls.add(startMinuteField);
                allErrorControls.add(endDatePicker);
                allErrorControls.add(endHourField);
                allErrorControls.add(endMinuteField);
                break;
            }

        }

        //Contact ID
        try {
            int contactID = (contactIDBox.getSelectionModel().getSelectedItem()).getContactID();
            modifyAppointment.setContactID(contactID);
        }catch(NullPointerException error) { allErrorControls.add(contactIDBox); }



        //Clear errors from anything on the errorFree Controls. This is done before the error controls, as a node might be error free in one area, but have an error in another.
        for(int i = 0; i < errorFreeControls.size(); i++) errorFreeControls.get(i).setStyle("");
        //Sets error FXs and returns false due to validation failing
        if(allErrorControls.size() > 0) {
            for(int i = 0; i < allErrorControls.size(); i++) allErrorControls.get(i).setStyle("-fx-border-color: red");
            return false;
        }
        modifyAppointment.setUserID(SessionData.getUserID());
        modifyAppointment.setLastUpdate(ZonedDateTime.now());
        modifyAppointment.setLastUpdateBy(SessionData.getUserName());

        return true;
    }
    /**
     * Will check for various data validations whenever a number of fields are updated within the view. Calls dynamicValidateData at the end, and will only return true if
     * dyamicValidateData() is true.
     * @return
     * @throws SQLException
     */
    @FXML
    private boolean validateData() throws SQLException {
        int characterLimit = 40;

        if(titleField.getText().length() >= characterLimit) {handleCharacterLimitError(titleErrorText, titleField);}
        else titleErrorText.setText("");
        if(locationField.getText().length() >= characterLimit) {handleCharacterLimitError(locationErrorText, locationField);}
        else locationErrorText.setText("");
        if(typeField.getText().length() >= characterLimit) {handleCharacterLimitError(typeErrorText, typeField);}
        else typeErrorText.setText("");
        if(descriptionField.getText().length() >= characterLimit) {handleCharacterLimitError(descriptionErrorText, descriptionField);}
        else descriptionErrorText.setText("");

        return dynamicValidateData();
    }
    /**
     * Validates the Dates and Time fields from the view, and if they are valid, it will return a ZonedDateTime with all of the data. Called by dynamicValidateData twice.
     * Once for the Start ZonedDateTime. The other for the End ZonedDateTime.
     * @param date the Date field to be validated
     * @param hour the Hour field to be validated
     * @param minute the Minute field to be validated
     * @param tOD the Time of Day combobox to be validated
     * @param errors the List of Nodes used by dyanmicValidateData that tracks nodes that cause errors
     * @param validNodes the List of Nodes used by dynamicValidateData that tracks nodes that are error free
     * @return ZonedDateTime if Valid, or null if invalid
     */
    private ZonedDateTime validateDateTime(DatePicker date, TextField hour, TextField minute, ComboBox<MorningAfternoon> tOD, ObservableList<Node> errors, ObservableList<Node> validNodes) {
        int initialErrorCount = errors.size();

        //Test Lengths of Hour and Minute
        if (hour.getText().length() == 0) errors.add(hour);
        else validNodes.add(hour);
        if (minute.getText().length() == 0) errors.add(minute);
        else validNodes.add(minute);

        //Make sure date and time of day isn't null
        if (date.getValue() == null) errors.add(date);
        else validNodes.add(date);
        if (tOD.getValue() == null) errors.add(tOD);
        else validNodes.add(tOD);

        //If the string in the hour or minute box is too long, shorten it
        if (hour.getText().length() > 2) {
            hour.setText(hour.getText().substring(0, 2));
            hour.positionCaret(2);
        }
        if (minute.getText().length() > 2) {
            minute.setText(minute.getText().substring(0, 2));
            minute.positionCaret(2);
        }

        if(errors.size() > initialErrorCount) return null;

        //If the method gets here, something is in the hour and minute boxes. Parsing at this point.
        int hourInt = 0;
        int minuteInt = 0;
        try{
            hourInt = Integer.parseInt(hour.getText());
        } catch(NumberFormatException e) {errors.add(hour);}
        try{
            minuteInt = Integer.parseInt(minute.getText());
        } catch(NumberFormatException e) {errors.add(minute);}

        if(errors.size() > initialErrorCount) return null;

        //If the method is this far, there should be integers for both the hour and minute, now to make sure they are in the proper range
        if(hourInt == 0) errors.add(hour);
        else if(hourInt < 12){
            if(tOD.getSelectionModel().getSelectedItem().isPM()) hourInt += 12;
        }
        else if(hourInt == 12){
            if(tOD.getSelectionModel().getSelectedItem().isAM()) hourInt = 0;
        }
        else errors.add(hour);

        if(minuteInt > 60 || minuteInt < 0) errors.add(minute);

        if(errors.size() > initialErrorCount) return null;

        //Test time against business hours
        LocalTime time = LocalTime.of(hourInt, minuteInt);
        long timeAsLong = time.getLong(ChronoField.SECOND_OF_DAY);
        DayOfWeek dayOfWeek = date.getValue().getDayOfWeek();
        LocalTime openTime = SessionData.getOpeningTime(dayOfWeek);
        LocalTime closeTime = SessionData.getClosingTime(dayOfWeek);
        long openTimeLong = openTime.getLong(ChronoField.SECOND_OF_DAY);
        long closeTimeLong = closeTime.getLong(ChronoField.SECOND_OF_DAY);

        if(timeAsLong < openTimeLong || timeAsLong > closeTimeLong) {
            Alert outsideHoursAlert = new Alert(Alert.AlertType.WARNING);
            outsideHoursAlert.setTitle("Outside Business Hours");
            outsideHoursAlert.setContentText("Appointment set outside of Business hours. Please choose a time between " + openTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                    + " - " + closeTime.format(DateTimeFormatter.ofPattern("h:mm a")));
            outsideHoursAlert.showAndWait();
            errors.add(hour);
            errors.add(minute);
            validNodes.add(date);
        }

        if(errors.size() > initialErrorCount) return null;

        LocalDateTime dateTime = LocalDateTime.of(date.getValue(), time);
        return dateTime.atZone(ZoneId.systemDefault());
    }
    /**
     * Called in validate data to check text field for a character limit
     * @param errorText The error Text where the error should be displayed in the view
     * @param textField The textfield to check for the character limit
     */
    private void handleCharacterLimitError(Text errorText, TextField textField){
        int maxCharacters = 40;
        String characterLimitError = "Max Length: 40 Characters";
        String shortenedText = textField.getText(0, maxCharacters);

        errorText.setText(characterLimitError);
        textField.setText(shortenedText);

        textField.positionCaret(maxCharacters);
    }
    /**
     * Computes the correct hour for the time of day from AM/PM and the hour on a 24 hour scale
     * @param hour
     * @param timeOfDay
     * @return correct hour on a 12 hour scale
     */
    private int updateTimeOfDay(int hour, MorningAfternoon timeOfDay){
        int updatedHour = hour;

        if(updatedHour > 12){
            timeOfDay.setPM();
            updatedHour -= 12;
        }
        else if(updatedHour == 12) timeOfDay.setPM();
        else if(updatedHour == 0) {
            updatedHour += 12;
            timeOfDay.setAM();
        }

        return updatedHour;
    }
    //endregion
    /**
     * Runs before everything else. Utilized to set up view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {modifyAppointment = AppointmentDAO.selectAppointmentByID(SessionData.getModifyID());}
        catch (SQLException e) {throw new RuntimeException(e);}

        try {
            customerIDBox.setItems(CustomerDAO.selectAllCustomers());
            for(int i = 0; i < customerIDBox.getItems().size(); i++){
                Customer currentCustomer = customerIDBox.getItems().get(i);
                if(currentCustomer.getId() == modifyAppointment.getCustomerID()) customerIDBox.getSelectionModel().select(currentCustomer);
            }
        }
        catch (SQLException e) {throw new RuntimeException(e);}

        contactIDBox.setItems(SessionData.getContacts());
        for(int i = 0; i < SessionData.getContacts().size(); i++){
            Contact contact = SessionData.getContacts().get(i);
            if(contact.getContactID() == modifyAppointment.getContactID()) contactIDBox.getSelectionModel().select(contact);
        }

        //Create the items for the AM/PM comboboxes
        ObservableList<MorningAfternoon> startTOD = FXCollections.observableArrayList();
        startTOD.add(new MorningAfternoon(MorningAfternoon.TimeOfDay.AM));
        startTOD.add(new MorningAfternoon(MorningAfternoon.TimeOfDay.PM));

        ObservableList<MorningAfternoon> endTOD = FXCollections.observableArrayList();
        endTOD.add(new MorningAfternoon(MorningAfternoon.TimeOfDay.AM));
        endTOD.add(new MorningAfternoon(MorningAfternoon.TimeOfDay.PM));

        startTODCombo.setItems(startTOD);
        endTODCombo.setItems(endTOD);

        //Populate form with the data from modifyAppointment
        idField.setText(String.valueOf(modifyAppointment.getAppointmentID()));
        titleField.setText(modifyAppointment.getTitle());
        locationField.setText(modifyAppointment.getLocation());
        typeField.setText(modifyAppointment.getType());
        descriptionField.setText(modifyAppointment.getDescription());

        startDatePicker.setValue(modifyAppointment.getStartDate().toLocalDate());
        endDatePicker.setValue(modifyAppointment.getEndDate().toLocalDate());
        int startHour = modifyAppointment.getStartDate().getHour();
        int endHour = modifyAppointment.getEndDate().getHour();
        MorningAfternoon startTimeOfDay = new MorningAfternoon(MorningAfternoon.TimeOfDay.AM);
        MorningAfternoon endTimeOfDay = new MorningAfternoon(MorningAfternoon.TimeOfDay.AM);
        startHour = updateTimeOfDay(startHour, startTimeOfDay);
        endHour = updateTimeOfDay(endHour, endTimeOfDay);

        startHourField.setText(String.valueOf(startHour));
        endHourField.setText(String.valueOf(endHour));

        startMinuteField.setText(String.valueOf(modifyAppointment.getStartDate().getMinute()));
        endMinuteField.setText(String.valueOf(modifyAppointment.getEndDate().getMinute()));

        startTODCombo.getSelectionModel().select(startTimeOfDay);
        endTODCombo.getSelectionModel().select(endTimeOfDay);
    }

}
