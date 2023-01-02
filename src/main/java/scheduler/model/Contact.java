package scheduler.model;

/**
 * Model class for Contacts. Corresponds with Contact Table.
 *
 * @author Alvin Roe
 */
public class Contact {
    /**Primary Key for the Contact*/
    private int contactID;
    /**Name of the contact*/
    private String contactName;
    /**Email address for the contact*/
    private String email;

    /**
     * Constructor for Contacts
     * @param contactID the Primary Key for the Contact
     * @param contactName name of the contact
     * @param email email address for the contact
     */
    public Contact(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }
    /**
     * @return Contact ID, which is also the Primary Key for the Contact from the Contacts Table
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Overrides toString for the readability of ComboBoxes
     * @return
     */
    @Override
    public String toString(){
        return contactID + ": " + contactName;
    }
}
