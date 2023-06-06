package model;

import java.time.LocalDate;

/**
 * Represents a caregiver, which is a type of user in the system.
 * Inherits properties and methods from the User class.
 */
public class Caregiver extends User {

    private long cid; // Caregiver ID

    /**
     * Constructs a caregiver with the given parameters.
     *
     * @param firstName   The first name of the caregiver.
     * @param surname     The surname of the caregiver.
     * @param phoneNumber The phone number of the caregiver.
     * @param archiveDate The archive date of the caregiver.
     */
    public Caregiver(String firstName, String surname, String phoneNumber, LocalDate archiveDate) {
        super(firstName, surname, phoneNumber, archiveDate);
    }

    /**
     * Constructs a caregiver with the given parameters.
     *
     * @param cid         The caregiver ID.
     * @param firstName   The first name of the caregiver.
     * @param surname     The surname of the caregiver.
     * @param phoneNumber The phone number of the caregiver.
     * @param archiveDate The archive date of the caregiver.
     */
    public Caregiver(long cid, String firstName, String surname, String phoneNumber, LocalDate archiveDate) {
        super(firstName, surname, phoneNumber, archiveDate);
        this.cid = cid;
    }

    /**
     * Returns the caregiver ID.
     *
     * @return The caregiver ID.
     */
    public long getCid() {
        return cid;
    }

    /**
     * Returns a string representation of the Caregiver object.
     *
     * @return A string containing the details of the caregiver.
     */
    @Override
    public String toString() {
        return "Caregiver" + "\nMNID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhonenumber: " + this.getPhoneNumber() +
                "\nArchiveDate: " + this.getArchiveDate() +
                "\n";
    }
}