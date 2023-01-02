package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Appointment;

import java.sql.*;
import java.time.ZoneId;

/**
 * Handles SQL logic for the Appointments table
 *
 * @author Alvin Roe
 */
public abstract class AppointmentDAO {
    /**Column number in Database for Appointment ID*/
    private static final int appointmentIDColumn = 1;
    /**Column number in Database for Title*/
    private static final int titleColumn = 2;
    /**Column number in Database for Description*/
    private static final int descriptionColumn = 3;
    /**Column number in Database for Location*/
    private static final int locationColumn = 4;
    /**Column number in Database for Type*/
    private static final int typeColumn = 5;
    /**Column number in Database for Start Date*/
    private static final int startColumn = 6;
    /**Column number in Database for End Date*/
    private static final int endColumn = 7;
    /**Column number in Database for Created Date*/
    private static final int createDateColumn = 8;
    /**Column number in Database for Created By*/
    private static final int createByColumn = 9;
    /**Column number in Database for Last Updated Date*/
    private static final int lastUpdateColumn = 10;
    /**Column number in Database for Last Updated By*/
    private static final int lastUpdateByColumn = 11;
    /**Column number in Database for Customer ID*/
    private static final int customerIDColumn = 12;
    /**Column number in Database for User ID*/
    private static final int userIDColumn = 13;
    /**Column number in Database for Contact ID*/
    private static final int contactIDColumn = 14;
    /**Used to track current ID being utilized in the system, so that numbers that have already been utilized will not be checked again until the next time the program loads*/
    private static int currentID = 1;

    /**
     * Takes each variable of the appointment object, and adds it to the database
     * @param appointment the appointment variable to add
     * @return the number of lines successfully added to the database
     * @throws SQLException
     */
    public static int insert(Appointment appointment) throws SQLException{
        String sql = "INSERT INTO APPOINTMENTS (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update," +
                "Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, appointment.getAppointmentID()); //Appointment ID
        preparedStatement.setString(2, appointment.getTitle()); //Title
        preparedStatement.setString(3, appointment.getDescription()); //Description
        preparedStatement.setString(4, appointment.getLocation()); //Location
        preparedStatement.setString(5, appointment.getType()); //Type
        preparedStatement.setTimestamp(6, new Timestamp(appointment.getStartDate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //Start Date
        preparedStatement.setTimestamp(7, new Timestamp(appointment.getEndDate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //End Date //TODO fix the offset system
        preparedStatement.setTimestamp(8, new Timestamp(appointment.getCreateDate().toInstant().toEpochMilli())); //Created Dated
        preparedStatement.setString(9, appointment.getCreatedBy()); //Created By
        preparedStatement.setTimestamp(10, new Timestamp(appointment.getLastUpdate().toInstant().toEpochMilli())); //Last Update
        preparedStatement.setString(11, appointment.getLastUpdateBy()); //Last Updated By
        preparedStatement.setInt(12, appointment.getCustomerID()); //Customer ID
        preparedStatement.setInt(13, appointment.getUserID()); //User ID
        preparedStatement.setInt(14, appointment.getContactID()); //Contact ID

        return preparedStatement.executeUpdate();
    }

    /**
     * Takes an Appointment object and updates the line in the Appointments table with the information that has the same Appointment ID.
     * @param appointment
     * @return Number of lines updated by Method
     * @throws SQLException
     */
    public static int update(Appointment appointment) throws SQLException{
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?," +
                "Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?," +
                "Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(14, appointment.getAppointmentID()); //Appointment ID
        preparedStatement.setString(1, appointment.getTitle()); //Title
        preparedStatement.setString(2, appointment.getDescription()); //Description
        preparedStatement.setString(3, appointment.getLocation()); //Location
        preparedStatement.setString(4, appointment.getType()); //Type
        preparedStatement.setTimestamp(5, new Timestamp(appointment.getStartDate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //Start Date
        preparedStatement.setTimestamp(6, new Timestamp(appointment.getEndDate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //End Date //TODO fix the offset system
        preparedStatement.setTimestamp(7, new Timestamp(appointment.getCreateDate().toInstant().toEpochMilli())); //Created Dated
        preparedStatement.setString(8, appointment.getCreatedBy()); //Created By
        preparedStatement.setTimestamp(9, new Timestamp(appointment.getLastUpdate().toInstant().toEpochMilli())); //Last Update
        preparedStatement.setString(10, appointment.getLastUpdateBy()); //Last Updated By
        preparedStatement.setInt(11, appointment.getCustomerID()); //Customer ID
        preparedStatement.setInt(12, appointment.getUserID()); //User ID
        preparedStatement.setInt(13, appointment.getContactID()); //Contact ID

        return preparedStatement.executeUpdate();
    }

    /**
     * Deletes the given Appointment with the given Appointment ID from the Appointments table
     * @param appointmentID
     * @return Number of lines deleted
     * @throws SQLException
     */
    public static int delete(int appointmentID) throws SQLException{
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, appointmentID);

        return preparedStatement.executeUpdate();
    }

    /**
     * Creates and returns an Observable List of the Appointments table. Data from the table is changed into the Appointment object.
     * @return Observable List of the Appointments table
     * @throws SQLException
     */
    public static ObservableList<Appointment> selectAllAppointments() throws SQLException{
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            allAppointments.add(new Appointment(
                    resultSet.getInt(appointmentIDColumn),
                    resultSet.getString(titleColumn),
                    resultSet.getString(descriptionColumn),
                    resultSet.getString(locationColumn),
                    resultSet.getString(typeColumn),
                    resultSet.getTimestamp(startColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getTimestamp(endColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getTimestamp(createDateColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getString(createByColumn),
                    resultSet.getTimestamp(lastUpdateColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getString(lastUpdateByColumn),
                    resultSet.getInt(customerIDColumn),
                    resultSet.getInt(userIDColumn),
                    resultSet.getInt(contactIDColumn)
            ));
        }
        return allAppointments;
    }

    /**
     * Creates and returns an Observable List of Appointments that have the given Customer ID, from the Appointments Table.
     * @param customerID the Customer ID for the Appointments to match.
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> selectAppointmentsByCustomerID(int customerID) throws SQLException{
        ObservableList<Appointment> appointmentsByCustomerID = FXCollections.observableArrayList();

        String sql = "SELECT * FROM APPOINTMENTS WHERE CUSTOMER_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            //if(((Appointment)resultSet).getCustomerID() != customerID) continue; TODO get rid of this if it is unneeded
            appointmentsByCustomerID.add(new Appointment(
                    resultSet.getInt(appointmentIDColumn),
                    resultSet.getString(titleColumn),
                    resultSet.getString(descriptionColumn),
                    resultSet.getString(locationColumn),
                    resultSet.getString(typeColumn),
                    resultSet.getTimestamp(startColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getTimestamp(endColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getTimestamp(createDateColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getString(createByColumn),
                    resultSet.getTimestamp(lastUpdateColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getString(lastUpdateByColumn),
                    resultSet.getInt(customerIDColumn),
                    resultSet.getInt(userIDColumn),
                    resultSet.getInt(contactIDColumn)
            ));
        }
        return appointmentsByCustomerID;
    }

    /**
     * Returns one specific Appointment based on the given Appointment ID
     * @param id The appointment ID for the Appointment to return
     * @return an Appointment object with the data for the given Appointment ID from the Appointments Table
     * @throws SQLException
     */
    public static Appointment selectAppointmentByID(int id) throws SQLException{
        Appointment matchingAppointment = null;

        String inquiry = "SELECT * FROM APPOINTMENTS WHERE APPOINTMENT_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(inquiry);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
            matchingAppointment = new Appointment(
                    resultSet.getInt(appointmentIDColumn),
                    resultSet.getString(titleColumn),
                    resultSet.getString(descriptionColumn),
                    resultSet.getString(locationColumn),
                    resultSet.getString(typeColumn),
                    resultSet.getTimestamp(startColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getTimestamp(endColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getTimestamp(createDateColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getString(createByColumn),
                    resultSet.getTimestamp(lastUpdateColumn).toLocalDateTime().atZone(ZoneId.systemDefault()),
                    resultSet.getString(lastUpdateByColumn),
                    resultSet.getInt(customerIDColumn),
                    resultSet.getInt(userIDColumn),
                    resultSet.getInt(contactIDColumn)
            );
        }

        return matchingAppointment;
    }

    /**
     * Creates and returns a new sequential ID Number for the Appointment.
     * @return
     * @throws SQLException
     */
    public static int generateNewID() throws SQLException {
        String sql = "SELECT Appointment_ID FROM APPOINTMENTS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            if(resultSet.getInt(appointmentIDColumn) == currentID){
                currentID = resultSet.getInt(appointmentIDColumn) + 1;
                generateNewID();
            }
        }
        return currentID;
    }
}
