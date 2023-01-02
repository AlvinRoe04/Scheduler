package scheduler.model;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Customer object modelled after the data table connected through CustomerDAO
 *
 * @author Alvin Roe
 */

public class Customer {
    /**ID for the Customer. Primary Key.*/
    private int id;
    /**Name of the Customer*/
    private String name;
    /**Address of the Customer*/
    private String address;
    /**Postal Code of the Customer*/
    private String postal;
    /**Phone Number of the Customer*/
    private String phone;
    /**Created Date and Time  of the Customer*/
    private ZonedDateTime createDate;
    /**User who created the customer*/
    private String author;
    /**Date and Time customer was updated*/
    private ZonedDateTime lastUpdate;
    /**User who updated the customer*/
    private String lastAuthor;
    /**First Level Division ID*/
    private int divisionID;

    /**
     * Customer constructor
     * @param id Primary Key
     * @param name
     * @param address
     * @param postal
     * @param phone
     * @param createDate
     * @param author
     * @param lastUpdate
     * @param lastAuthor
     * @param divisionID First Level Division ID
     */
    public Customer(int id, String name, String address, String postal, String phone, ZonedDateTime createDate, String author, ZonedDateTime lastUpdate, String lastAuthor, int divisionID) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.createDate = createDate;
        this.author = author;
        this.lastUpdate = lastUpdate;
        this.lastAuthor = lastAuthor;
        this.divisionID = divisionID;
    }

    /**
     * Constructor for an empty customer
     */
    public Customer(){
        id = 0;
        name = "";
        address = "";
        postal = "";
        phone = "";
        createDate = ZonedDateTime.now();
        author = "";
        lastUpdate = ZonedDateTime.now();
        lastAuthor = "";
        divisionID = 0;
    }

    /**
     *
     * @return the ID for the Customer, Primary Key
     */
    public int getId() {
        return id;
    }

    /**
     * Changes the Customer ID
     * @param id value of the customer id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     *
     * @return name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the customer
     * @param name text to set the name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the address for the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * Changes the address for the customer
     * @param address the text for the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return the postal code for the customer
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Changes the postal code for the customer
     * @param postal text of the postal code
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     *
     * @return Phone number for the customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Changes the phone number for the customer
     * @param phone text for the phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return Date and Time that this was created
     */
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Changes the date and time this was created
     * @param date The date and time that this was created
     */
    public void setCreateDate(ZonedDateTime date) {
        this.createDate = date;
    }

    /**
     *
     * @return user who created this Customer
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Changes the author of this customer
     * @param author the text for the user who created this customer
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return Date and Time the customer was last updated
     */
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Changed the date and time that the customer was last updated
     * @param lastUpdate Date and Time that customer was last updated
     */
    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     *
     * @return last user that updated the customer
     */
    public String getLastAuthor() {
        return lastAuthor;
    }

    /**
     * Changes the user that last updated the customer
     * @param lastAuthor text of the user that last updated the customer
     */
    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    /**
     *
     * @return First Level Division ID for customer
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Changes the first level division id for the customer
     * @param divisionID the value of the first level division id
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * Readable version of the Customer data
     * @return ID and Name of customer
     */
    @Override
    public String toString(){
        return id + ": " + name;
    }
}
