package scheduler.model;
import java.time.*;

/**
 * Model object for an Appointment. Corresponds with data from the Appointments Table.
 *
 * @author Alvin Roe
 */
public class Appointment {
    /**Appointment ID of the Appointment*/
    private int appointmentID;
    /**Title of the Appointment*/
    private String title;
    /**Description of the Appointment*/
    private String description;
    /**Location of the Appointment*/
    private String location;
    /**Type of the Appointment*/
    private String type;
    /**Start Date of the Appointment*/
    private ZonedDateTime startDate;
    /**End Date of the Appointment*/
    private ZonedDateTime endDate;
    /**Created Date of the Appointment*/
    private ZonedDateTime createDate;
    /**Created by of the Appointment*/
    private String createdBy;
    /**Last Updated date of the Appointment*/
    private ZonedDateTime lastUpdate;
    /**User who Last Updated the Appointment*/
    private String lastUpdateBy;
    /**Customer ID who is participating in this Appointment*/
    private int customerID;
    /**User ID for the person who set up this Appointment*/
    private int userID;
    /**Contact ID who is the contact for this Appointment*/
    private int contactID;

    /**
     * Constructor for Appointment
     * @param appointmentID Primary Key for Appointment
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDate
     * @param endDate
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     * @param customerID The ID for the Customer who this appointment is for
     * @param userID The user ID of the person logged in to set up this appointment
     * @param contactID The contact ID for the contact for this appointment
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       ZonedDateTime startDate, ZonedDateTime endDate, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate,
                       String lastUpdateBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Constructor for an empty appointment
     */
    public Appointment(){
        appointmentID = 0;
        title = "";
        description = "";
        location = "";
        type = "";
        startDate = ZonedDateTime.now();
        endDate = ZonedDateTime.now();
        createDate = ZonedDateTime.now();
        createdBy = "";
        lastUpdate = ZonedDateTime.now();
        lastUpdateBy = "";
        customerID = 0;
        userID = 0;
        contactID = 0;
    }

    /**
     * Returns the appointment ID
     * @return the appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Changes Appointment ID
     * @param appointmentID value for the Appointment ID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Returns the Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes title
     * @param title text for the Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the Description
     * @return the description of the appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description for the appointment
     * @param description text for the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the location for the appointment
     * @return the location for the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the location
     * @param location text for the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type of appointment
     */
    public String getType() {
        return type;
    }

    /**
     * Changes the type of appointment
     * @param type text for the type of appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Start Date of the Appointment
     */
    public ZonedDateTime getStartDate(){
        return startDate;
    }

    /**
     * Sets the Start Date and Time of the appointment
     * @param startDate start date and time of the appointment
     */
    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * @return End date and time of the appointment
     */
    public ZonedDateTime getEndDate() {
        return endDate;
    }

    /**
     * Changes the end date and time of the appointment
     * @param endDate end date and time of the appointment
     */
    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * @return Date and Time this appointment was created
     */
    public ZonedDateTime getCreateDate() {
        return createDate;
    }
    /**
     *Changes the Created Date and Time for the apppointment
     * @param createDate the Date and Time the appointment was created on
     */
    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return The user who created this appointment
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *Changes the Created By for this appointment
     * @param createdBy The user who created this appointment
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return date and time this appointment was last updated on
     */
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Changes the last updated date and time
     * @param lastUpdate the date and time this appointment was last updated on
     */
    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the user who last updated the appointment
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * Changes the user who last updated the appointment
     * @param lastUpdateBy the user who last updated appointment
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * @return the customer id
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Changes the customer id
     * @param customerID value of the customer id
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return User ID that made appointment
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Changes the user ID
     * @param userID value of the user id
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return contact id
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Changes the contact id
     * @param contactID value of the contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
