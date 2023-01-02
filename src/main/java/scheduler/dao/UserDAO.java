package scheduler.dao;

import scheduler.helper.SessionData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Accesses information from the Data table "users". Mainly used for the Login Screen.
 *
 * @author Alvin Roe
 */
public abstract class UserDAO {
    /**The column number for the user id in the Users database*/
    private static final int userIDColumn = 1;
    /**The column number for the Username in the Users database*/
    private static final int userNameColumn = 2;
    /**The column number for the password in the Users database*/
    private static final int userPasswordColumn = 3;

    /**
     * Checks a given username and password against the database to see if it is a valid user.
     * @param userName the username to check against the database
     * @param password the password to check against the database
     * @return true if there was a match in a row of the database, or false if there was not
     * @throws SQLException
     */
    public static boolean authenticate(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String queryUserName = resultSet.getString(userNameColumn);
            String queryPassword = resultSet.getString(userPasswordColumn);
            if(queryUserName.equals(userName) && queryPassword.equals(password)){
                SessionData.setUserID(resultSet.getInt(userIDColumn));
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the current username of the current user based on the user ID from SessionData
     * @return username of the current user based on the user ID
     * @throws SQLException
     */
    public static String currentUserName() throws SQLException {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, SessionData.getUserID());

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            if(resultSet.getInt(userIDColumn) == SessionData.getUserID()) return resultSet.getString(userNameColumn);
        }

        return null;
    }

    /**
     * Creates and returns a Hashtable where the Key is the User ID and the Value is the User Name
     * @return Hashtable where the Key is the User ID and the Value is the User Name
     * @throws SQLException
     */
    public static Hashtable<Integer, String> getUserTable() throws SQLException {
        Hashtable<Integer, String> userTable = new Hashtable<>();

        String sql = "SELECT * FROM USERS";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next())
            userTable.put(resultSet.getInt(userIDColumn), resultSet.getString(userNameColumn));

        return userTable;
    }
}
