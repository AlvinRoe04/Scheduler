package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Class to obtain Contact information from the Contacts Table.
 *
 * @author Alvin Roe
 */
public abstract class ContactDAO {
    /**Column number for the Contact ID column in the Contacts Table*/
    private static final int contactIDColumn = 1;
    /**Column number for the Name column in the Contacts Table*/
    private static final int contactNameColumn = 2;
    /**Column number for the Email column in the Contacts Table*/
    private static final int contactEmailColumn = 3;

    /**
     * Creates and returns an Observable list of Contacts based on the data in the Contacts Table
     * @return Observable list of Contacts based on the data in the Contacts Table
     * @throws SQLException
     */
    public static ObservableList<Contact> selectAllContacts()throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            contacts.add(new Contact(
                    resultSet.getInt(contactIDColumn),
                    resultSet.getString(contactNameColumn),
                    resultSet.getString(contactEmailColumn)
            ));
        }
        return contacts;
    }

    /**
     * Returns a Hashtable with the Contact ID as the Key and the Contact Name as the Value
     * @return Hashtable with the Contact ID as the Key and the Contact Name as the Value
     * @throws SQLException
     */
    public static Hashtable<Integer, String> getHashtableContacts()throws SQLException {
        Hashtable<Integer, String> contactTable = new Hashtable<>();

        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            contactTable.put(resultSet.getInt(contactIDColumn), resultSet.getString(contactNameColumn));
        }
        return contactTable;
    }
}
