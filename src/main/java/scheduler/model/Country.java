package scheduler.model;

/**
 * Model class for Country. Corresponds with data from the Country Table.
 *
 * @author Alvin Roe
 */
public class Country {
    /**Primary Key for the Country Table*/
    private int countryID;
    /**Name of the Country*/
    private String countryName;

    /**
     * Constructor for Country
     * @param countryID ID for Country, the Primary Key
     * @param countryName Name of the Country
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * @return Country ID, the Primary Key
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Overrides toString for the readability of data
     * @return Country Name
     */
    @Override
    public String toString(){
        return countryName;
    }




}
