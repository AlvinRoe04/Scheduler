package scheduler.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.dao.ContactDAO;
import scheduler.dao.DivisionCountryDAO;
import scheduler.dao.UserDAO;
import scheduler.model.*;

import java.sql.SQLException;
import java.time.*;
import java.util.Hashtable;

/**
 * Holds variables for the current login session. Much of data is meant to minimize the amount of Database connections utilized by the app.
 *
 * @author Alvin Roe
 */
public abstract class SessionData {
    /**Hashtable for the opening hours throughout the week*/
    private static Hashtable<DayOfWeek, LocalTime> openHours = new Hashtable<>();
    /**Hashtable for the closing hours throughout the week*/
    private static Hashtable<DayOfWeek, LocalTime> closeHours = new Hashtable<>();
    /**The current user_id from the Users table in the database. Usually set by the UserDAO class.*/
    private static int userID;
    /**
     * List of list of First Level Divisions separated by Country IDs
     */
    private static ObservableList<ObservableList<FirstLevelDivision>> firstLevelDivisionsSeparated;
    /**List of all the First Level Divisions*/
    private static ObservableList<FirstLevelDivision> firstLevelDivisions;
    /**List of all of the Countries*/
    private static ObservableList<Country> countries;
    /**The ID that is currently being Updated. Utilized for both Customers and Appointments*/
    private static int modifyID;
    /**Table of Contacts, keys are ID and values are names*/
    private static Hashtable<Integer, String> contactTable = new Hashtable<>();
    /**Table of Usernames, keys are ID and values are names*/
    private static Hashtable<Integer, String> userNameTable = new Hashtable<>();
    /**List of all the contacts*/
    private static ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * Gets the User ID that logged in for the current session.
     * @return user ID
     */
    public static int getUserID() { return userID; }

    /**
     * Sets the User ID for the current session.
     * @param newID the new User ID that needs to be set.
     */
    public static void setUserID(int newID) {userID = newID;}

    /**
     * Creates and returns a list of ObservableLists for First Level divisions separated by Country ID
     * @return list of ObservableLists for First Level divisions separated by Country ID
     */
    public static ObservableList<ObservableList<FirstLevelDivision>> getSeparatedFirstLevelDivisions(){
        return firstLevelDivisionsSeparated;
    }
    /**
     * Creates and returns a list of all First Level Divisions
     * @return list of all First Level Divisions
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions(){
        return firstLevelDivisions;
    }
    /**
     *Creates and returns a list of all countries
     * @return list of all Countries
     */
    public static ObservableList<Country> getAllCountries(){
        return countries;
    }
    /**
     * Method called within the initialize method for the Schedule. It is only ran once.
     * @throws SQLException
     */
    public static void InitializeSessionData() throws SQLException {
        //Create Hashtables for needed data
        contactTable = ContactDAO.getHashtableContacts();
        userNameTable = UserDAO.getUserTable();

        //Get lists from the various DAOs
        firstLevelDivisionsSeparated = DivisionCountryDAO.getSeparatedFirstLevelDivisions();
        firstLevelDivisions = DivisionCountryDAO.getAllFirstLevelDivisions();
        countries = DivisionCountryDAO.getAllCountries();
        contacts = ContactDAO.selectAllContacts();

        //Set hours of operation, currently set for 8am - 10pm EST. setHoursForDay does change the given hours from EST to the Zone ID time set on the local machine
        LocalDateTime openingTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));
        LocalDateTime closingTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));

        ZonedDateTime openingTimeEST = ZonedDateTime.of(openingTime, ZoneId.of("America/New_York"));
        ZonedDateTime closingTimeEST = ZonedDateTime.of(closingTime, ZoneId.of("America/New_York"));

        setHoursForDay(DayOfWeek.MONDAY, 8, 0, openHours);
        setHoursForDay(DayOfWeek.TUESDAY, 8, 0, openHours);
        setHoursForDay(DayOfWeek.WEDNESDAY, 8, 0, openHours);
        setHoursForDay(DayOfWeek.THURSDAY, 8, 0, openHours);
        setHoursForDay(DayOfWeek.FRIDAY, 8, 0, openHours);
        setHoursForDay(DayOfWeek.SATURDAY, 8, 0, openHours);
        setHoursForDay(DayOfWeek.SUNDAY, 8, 0, openHours);

        setHoursForDay(DayOfWeek.MONDAY,22, 0, closeHours);
        setHoursForDay(DayOfWeek.TUESDAY,22, 0, closeHours);
        setHoursForDay(DayOfWeek.WEDNESDAY,22, 0, closeHours);
        setHoursForDay(DayOfWeek.THURSDAY,22, 0, closeHours);
        setHoursForDay(DayOfWeek.FRIDAY,22, 0, closeHours);
        setHoursForDay(DayOfWeek.SATURDAY,22, 0, closeHours);
        setHoursForDay(DayOfWeek.SUNDAY,22, 0, closeHours);
    }

    /**
     * Helper method used to assist the Initialize method with setting up the business hours
     * @param day
     * @param openingHour
     * @param openingMinute
     * @param hoursTable
     */
    private static void setHoursForDay(DayOfWeek day, int openingHour, int openingMinute, Hashtable<DayOfWeek, LocalTime> hoursTable){
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(openingHour, openingMinute));
        ZonedDateTime dateTimeEST = ZonedDateTime.of(dateTime, ZoneId.of("America/New_York"));
        hoursTable.put(day, dateTimeEST.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
    }

    /**
     * Returns the Modify ID, which is the ID for the line on the table when Update button is pressed
     * @return the Customer ID if the Customer Update button was pressed, or the Appointment ID if the Appointment Update button is pressed
     */
    public static int getModifyID() {
        return modifyID;
    }

    /**
     * Sets the Modify ID
     * @param modifyID the value for the new Modify ID
     */
    public static void setModifyID(int modifyID) {
        SessionData.modifyID = modifyID;
    }

    /**
     * Gets the Country ID for the given First Level Division ID
     * @param divisionID First Level Division ID within the country
     * @return Country ID for the given First Level Division ID
     */
    public static int getCountryID(int divisionID){
        int countryID;

        for(int countryIndex = 0; countryIndex < firstLevelDivisionsSeparated.size(); countryIndex++){
            for(int divisionIndex = 0; divisionIndex < firstLevelDivisionsSeparated.get(countryIndex).size(); divisionIndex++){
                if(firstLevelDivisionsSeparated.get(countryIndex).get(divisionIndex).getDivisionID() == divisionID)
                    return firstLevelDivisionsSeparated.get(countryIndex).get(divisionIndex).getCountryID();
            }
        }
        return -1;
    }

    /**
     * Contact Name for the Contact ID
     * @param contactID
     * @return Contact Name for the given Contact ID
     */
    public static String getContactName(int contactID){
        return contactTable.get(contactID);
    }

    /**
     * Username for the User ID. User ID is tracked during the login process, and is saved in SessionData.
     * @return Username for the UserID that is currently logged in
     */
    public static String getUserName(){
        return userNameTable.get(userID);
    }

    /**
     * Returns a list of all contacts
     * @return a list of all contacts
     */
    public static ObservableList<Contact> getContacts() {
        return contacts;
    }

    /**
     * Returns the opening time for the given day of the week
     * @param day day of the week for the opening time
     * @return opening time for the given day
     */
    public static LocalTime getOpeningTime(DayOfWeek day){
        return openHours.get(day);
    }
    /**
     * Returns the closing time for the given day of the week
     * @param day day of the week for the closing time
     * @return closing time for the given day
     */
    public static LocalTime getClosingTime(DayOfWeek day){
        return closeHours.get(day);
    }

    /**
     * Sets the contacts
     * @param contacts
     */
    public static void setContacts(ObservableList<Contact> contacts) {
        SessionData.contacts = contacts;
    }
}
