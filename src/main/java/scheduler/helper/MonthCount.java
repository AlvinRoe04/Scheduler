package scheduler.helper;

import java.time.Month;

/**
 * Holds data for the Month that is being counted, the count of that Month (how many of them there are) and a String version of the name. Utilized for Reports
 *
 * @author Alvin Roe
 */
public class MonthCount {
    /**The month that is being counted*/
    private Month month;
    /**A count of the month*/
    private int count;
    /**The String name of the Month*/
    private String name;
    /**Default Constructor for MonthCount*/
    public MonthCount(Month month, int count, String name) {
        this.month = month;
        this.count = count;
        this.name = name;
    }

    /**
     * Returns the Month
     * @return the month
     */
    public Month getMonth() {
        return month;
    }
    /**
     * Sets the month
     * @param month the month to set the month to
     */
    public void setMonth(Month month) {
        this.month = month;
    }

    /**
     * Returns the count
     * @return count of the month
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the count
     * @param count the amount to set the count to
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Returns the name of the Month how it should be written
     * @return the name of the month
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the month
     * @param name how the name of the month should be written
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds an amount to the count
     * @param amountToAdd the amount to add to the count
     */
    public void addToCount(int amountToAdd) { count = count + amountToAdd; }
}
