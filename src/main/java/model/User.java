package model;

import java.time.LocalDate;

public class User extends Person {
    private String phoneNumber;

    /**
     * constructs a user from the given params.
     *
     * @param firstName
     * @param surname
     * @param phoneNumber
     */

    public User(String firstName, String surname, String phoneNumber, LocalDate archiveDate) {
        super(firstName, surname, archiveDate);
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the caregivers phone number
     * @return
     */

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the caregivers phone number
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
