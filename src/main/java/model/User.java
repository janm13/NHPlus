package model;

import java.time.LocalDate;

/**
 * The `User` class represents a user entity, which is a type of person with an associated phone number.
 * It extends the `Person` class and inherits its fields and methods.
 */
public class User extends Person {
    private String phoneNumber;

    /**
     * Constructs a new `User` object with the given parameters.
     *
     * @param firstName   The first name of the user.
     * @param surname     The surname of the user.
     * @param phoneNumber The phone number of the user.
     * @param archiveDate The archive date of the user.
     */
    public User(String firstName, String surname, String phoneNumber, LocalDate archiveDate) {
        super(firstName, surname, archiveDate);
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the phone number of the caregiver.
     *
     * @return The phone number of the caregiver.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the caregiver.
     *
     * @param phoneNumber The new phone number of the caregiver.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}