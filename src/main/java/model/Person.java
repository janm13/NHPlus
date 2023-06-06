package model;

import utils.DateConverter;

import java.time.LocalDate;

/**
 * Represents a person with a first name, surname, and archive date.
 * This is an abstract class and cannot be instantiated directly.
 */
public abstract class Person {
    private String firstName; // First name of the person
    private String surname; // Surname of the person
    private LocalDate archiveDate; // Archive date of the person

    /**
     * Constructs a Person object with the given parameters.
     *
     * @param firstName     The first name of the person.
     * @param surname       The surname of the person.
     * @param archiveDate   The archive date of the person.
     */
    public Person(String firstName, String surname, LocalDate archiveDate) {
        this.firstName = firstName;
        this.surname = surname;
        this.archiveDate = archiveDate;
    }

    /**
     * Returns the first name of the person.
     *
     * @return The first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the person.
     *
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the surname of the person.
     *
     * @return The surname of the person.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the person.
     *
     * @param surname The new surname.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the archive date as a string.
     *
     * @return The archive date as a string in the format "YYYY-MM-DD", or null if the archive date is not set.
     */
    public String getArchiveDate() {
        if (archiveDate == null) {
            return null;
        } else {
            return archiveDate.toString();
        }
    }

    /**
     * Sets the archive date of the person using the given string representation.
     *
     * @param s_date The archive date as a string in the format "YYYY-MM-DD".
     */
    public void setArchiveDate(String s_date) {
        LocalDate archiveDate = DateConverter.convertStringToLocalDate(s_date);
        this.archiveDate = archiveDate;
    }
}