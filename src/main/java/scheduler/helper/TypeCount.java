package scheduler.helper;

/**
 * Holds data for the Type that is being counted, and the number of those types that has been counted. Utilized for Reports.
 *
 * @author Alvin Roe
 */
public class TypeCount {
    /**Type being counted*/
    private String type;
    /**Amount of the type that has been counted*/
    private int count;

    /**
     * Default Constructor for TypeCount
     * @param type the type being counted
     * @param count the amount of that type that has been counted
     */
    public TypeCount(String type, int count) {
        this.type = type;
        this.count = count;
    }

    /**
     * Returns the type that is being counted
     * @return type that is being counted
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the Type that is being counted
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * The amount of that type that is being counted
     * @return the amount of the type being used
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the amount of the type that is being counted
     * @param count the amount to set the count to
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Increases the account by the given amount
     * @param amountToAdd amount to add to count
     */
    public void addToCount(int amountToAdd){
        count += amountToAdd;
    }

    /**
     * Overrides toString to just print the Type. Set up for readability in comboboxes
     * @return the type being counted
     */
    @Override
    public String toString(){
        return type;
    }
}
