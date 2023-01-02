package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Used to access and manipulate the Customers Table
 *
 * @author Alvin Roe
 */
public class CustomerDAO {
    /**Column number for the ID column*/
    private static final int ID_COLUMN = 1;
    /**Column number for the Name column*/
    private static final int NAME_COLUMN = 2;
    /**Column number for the Address column*/
    private static final int ADDRESS_COLUMN = 3;
    /**Column number for the Postal Code column*/
    private static final int POSTAL_COLUMN = 4;
    /**Column number for the Phone column*/
    private static final int PHONE_COLUMN = 5;
    /**Column number for the Created Date column*/
    private static final int CREATE_DATE_COLUMN = 6;
    /**Column number for the Created By column*/
    private static final int CREATED_BY_COLUMN = 7;
    /**Column number for the Last Updated Date column*/
    private static final int LAST_UPDATE_COLUMN = 8;
    /**Column number for the Last Updated By column*/
    private static final int LAST_UPDATE_BY_COLUMN = 9;
    /**Column number for the First Level Division ID column*/
    private static final int DIVISION_ID_COLUMN = 10;
    /**Used to track current ID being utilized in the system, so that numbers that have already been utilized will not be checked again until the next time the program loads*/
    private static int currentID = 1;

    /**
     * Takes each variable of the Customer object, and adds it to the database
     * @param customer The customer Object that holds the data to be added to the database
     * @return Number of lines successfully added to the database
     * @throws SQLException
     */
    public static int insert(Customer customer) throws SQLException{
        String sql = "INSERT INTO Customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date," +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, customer.getId()); //ID
        preparedStatement.setString(2, customer.getName()); //Name
        preparedStatement.setString(3, customer.getAddress()); //Address
        preparedStatement.setString(4, customer.getPostal()); //Postal Code
        preparedStatement.setString(5, customer.getPhone()); //Phone Number
        preparedStatement.setTimestamp(6, new Timestamp(customer.getCreateDate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //Create Date
        preparedStatement.setString(7, customer.getAuthor()); //Created By
        preparedStatement.setTimestamp(8, new Timestamp(customer.getLastUpdate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //Last Update Date
        preparedStatement.setString(9, customer.getLastAuthor()); //Last Updated By
        preparedStatement.setInt(10, customer.getDivisionID()); //Division ID

        return preparedStatement.executeUpdate();
    }

    /**
     * Takes a Customer object and updates the line in the Customers table with the information that has the same Customer ID.
      * @param customer customer to add to the database
     * @return number of lines added to the database
     * @throws SQLException
     */
    public static int update(Customer customer) throws SQLException{
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?," +
                "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ?  WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(10, customer.getId()); //ID
        preparedStatement.setString(1, customer.getName()); //Name
        preparedStatement.setString(2, customer.getAddress()); //Address
        preparedStatement.setString(3, customer.getPostal()); //Postal Code
        preparedStatement.setString(4, customer.getPhone()); //Phone Number
        preparedStatement.setTimestamp(5, new Timestamp(customer.getCreateDate().withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli())); //Create Date
        preparedStatement.setString(6, customer.getAuthor()); //Created By
        preparedStatement.setTimestamp(7, new Timestamp(ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli())); //Last Update Date
        preparedStatement.setString(8, customer.getLastAuthor()); //Last Updated By
        preparedStatement.setInt(9, customer.getDivisionID()); //Division ID

        return preparedStatement.executeUpdate();
    }

    /**
     * Deletes the given Customer with the given Customer ID from the Customers table
     * @param customerID customer ID for the line to be deleted from the Customers Table
     * @return number of lines deleted
     * @throws SQLException
     */
    public static int delete(int customerID) throws SQLException{
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerID);

        return preparedStatement.executeUpdate();
    }

    /**
     * Creates and returns an Observable List of the Customers table. Data from the table is changed into the Customer object.
     * @return Observable List of the Customers table.
     * @throws SQLException
     */
    public static ObservableList<Customer> selectAllCustomers() throws SQLException{
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            customers.add(convertToCustomer(resultSet));
        }
        return customers;
    }

    /**
     * Helper Method to convert ResultSet data into a Customer object
     * @param resultSet data from the Customers table to be changed into Customer Object
     * @return Customer based on the result set data.
     * @throws SQLException
     */
    private static Customer convertToCustomer(ResultSet resultSet) throws SQLException {

        return new Customer(
        resultSet.getInt(ID_COLUMN), //ID
        resultSet.getString(NAME_COLUMN), //Name
        resultSet.getString(ADDRESS_COLUMN), //Address
        resultSet.getString(POSTAL_COLUMN), //Postal Code
        resultSet.getString(PHONE_COLUMN), //Phone Number
        resultSet.getTimestamp(CREATE_DATE_COLUMN).toLocalDateTime().atZone(ZoneId.systemDefault()), //Create Date
        resultSet.getString(CREATED_BY_COLUMN), //Created By
        resultSet.getTimestamp(LAST_UPDATE_COLUMN).toLocalDateTime().atZone(ZoneId.systemDefault()), //Last Update Date
        resultSet.getString(LAST_UPDATE_BY_COLUMN), //Last Updated By
        resultSet.getInt(DIVISION_ID_COLUMN)); //Division ID
    }

    /**
     * Generates a new sequential ID for the Customer
     * @return a new sequential ID for the Customer
     * @throws SQLException
     */
    public static int generateNewID() throws SQLException {
        String sql = "SELECT Customer_ID FROM CUSTOMERS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            if(resultSet.getInt(ID_COLUMN) == currentID){
                currentID = resultSet.getInt(ID_COLUMN) + 1;
                generateNewID();
            }
        }
        return currentID;
    }

}

