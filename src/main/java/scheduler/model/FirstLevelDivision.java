package scheduler.model;

/**
 * Model class for First Level Divisions, states for the USA, Provinces for Canada, ect. Corresponds with the FirstLevelDivision Table.
 *
 * @author Alvin Roe
 */
public class FirstLevelDivision {
    //region Variables
    /**Primary Key. First Level Division ID*/
    private int divisionID;
    /**Name of the First Level Division*/
    private String division;
    /**Country ID that the First Level Division is within*/
    private int countryID;
    //endregion

    //region Constructor

    /**
     * Constructor for First Level Division
     * @param divisionID Primary Key
     * @param division
     * @param countryID
     */
    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }
    //endregion

    //region Getters and Setters

    /**
     *
     * @return First Level Division ID, Primary Key
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @return First Level Division name
     */

    public String getDivision() {
        return division;
    }

    /**
     * Changes the First Level Division name
     * @param division text for the First Level Division name
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *
     * @return Country ID that the First Level Division is within
     */
    public int getCountryID() {
        return countryID;
    }
    //endregion

    /**
     *
     * @return Name of First Level Division
     */
    @Override
    public String toString(){
        return division;
    }
}
