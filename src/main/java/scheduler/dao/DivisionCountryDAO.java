package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.model.Country;
import scheduler.model.FirstLevelDivision;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gets and manipulates data in both the Country and First Level Divisions tables.
 *
 * @author Alvin Roe
 */
public class DivisionCountryDAO {
    /**The column number for the division id in the First Level Division database*/
    private static final int idColumn = 1;
    /**The column number for the division in the First Level Division database*/
    private static final int nameColumn = 2;
    /**The column number for the customer id in the First Level Division database*/
    private static final int divisionCountryIDColumn = 7;

    /**
     * Creates a list of lists of First Level Divisions. Each list is separated based on Country ID.
     * @return  list of lists of First Level Divisions separated based on Country ID
     * @throws SQLException
     */
    public static ObservableList<ObservableList<FirstLevelDivision>> getSeparatedFirstLevelDivisions() throws SQLException {
        ObservableList<ObservableList<FirstLevelDivision>> firstLevelDivisions = FXCollections.observableArrayList();
        ObservableList<Country> countries = getAllCountries();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        ResultSet resultSet= JDBC.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery();

        for(int currentID = 1; currentID <= countries.size(); currentID++){
            resultSet.beforeFirst();
            ObservableList<FirstLevelDivision> countryDivisions = FXCollections.observableArrayList();
            while(resultSet.next()){
                if(resultSet.getInt(divisionCountryIDColumn) == currentID)
                    countryDivisions.add(new FirstLevelDivision(resultSet.getInt(idColumn), resultSet.getString(nameColumn), resultSet.getInt(divisionCountryIDColumn)));
            }
            firstLevelDivisions.add(countryDivisions);
        }

        return firstLevelDivisions;
    }

    /**
     * Gets one large list of all the First Level Divisions
     * @return list of all the First Level Divisions
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        ResultSet resultSet= JDBC.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery();
        while(resultSet.next()){
                 firstLevelDivisions.add(new FirstLevelDivision(resultSet.getInt(idColumn), resultSet.getString(nameColumn), resultSet.getInt(divisionCountryIDColumn)));
        }
        return firstLevelDivisions;
    }

    /**
     * Returns a list of Countries from Country Table
     * @return list of Countries from Country Table
     * @throws SQLException
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String sql = "SELECT * FROM COUNTRIES";
        ResultSet resultSet= JDBC.connection.prepareStatement(sql).executeQuery();
        while(resultSet.next()){
            countries.add(new Country(resultSet.getInt(idColumn), resultSet.getString(nameColumn)));
        }
        return countries;
    }
}
