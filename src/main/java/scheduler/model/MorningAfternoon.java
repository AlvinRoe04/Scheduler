package scheduler.model;

/**
 * Class to assist with ComboBoxes, tracking AM/PM data.
 *
 * @author Alvin Roe
 */
public class MorningAfternoon {
    /**Enum for either AM PM*/
    public enum TimeOfDay {AM, PM}
    private TimeOfDay timeOfDay = TimeOfDay.AM;

    /**
     * Constructor for MorningAfternoon
     * @param timeOfDay AM or PM
     */
    public MorningAfternoon(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    /**
     * Checks if this object is set to AM
     * @return true if it is AM, false if not AM
     */
    public Boolean isAM(){
        if(timeOfDay == TimeOfDay.AM) return true;
        return false;
    }

    /**
     * Checks if this object is set to PM
     * @return true if it is PM, false if not PM
     */
    public Boolean isPM(){
        if(timeOfDay == TimeOfDay.PM) return true;
        return false;
    }

    /**
     * Overrides ToString for readability, shows AM or PM depending on enum state
     * @return AM or PM based on timeOfDay
     */
    @Override
    public String toString(){
        if(timeOfDay == TimeOfDay.AM) return "AM";
        return "PM";
    }

    /**
     * Changes MorningAfternoon to PM
     */
    public void setPM(){
        timeOfDay = TimeOfDay.PM;
    }

    /**
     * Changes MorningAfternoon to AM
     */
    public void setAM(){
        timeOfDay = TimeOfDay.AM;
    }

}
