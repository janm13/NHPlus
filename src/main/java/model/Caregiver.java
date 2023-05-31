package model;

import java.time.LocalDate;

public class Caregiver extends User {

    private long cid;

    /**
     * constructs a caregiver from the given params.
     *
     * @param firstName
     * @param surname
     * @param phoneNumber
     */
    public Caregiver (String firstName, String surname, String phoneNumber, LocalDate archiveDate) {
        super(firstName, surname, phoneNumber, archiveDate);
    }

    /**
     * constructs a caregiver from the given params.
     *
     * @param cid
     * @param firstName
     * @param surname
     * @param phoneNumber
     */
    public Caregiver (long cid, String firstName, String surname, String phoneNumber, LocalDate archiveDate) {
        super(firstName, surname, phoneNumber, archiveDate);
        this.cid = cid;
    }

    /**
     *
     * @return caregiver id
     */
    public long getCid() { return cid; }

    /**
     *
     * @return string-representation of the Caregiver
     */
    public String toString() {
        return "Caregiver" + "\nMNID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhonenumber: " + this.getPhoneNumber() +
                "\nArchiveDate: " + this.getArchiveDate() +
                "\n";
    }
}
