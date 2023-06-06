package model;

import utils.DateConverter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in a nursing home who is treated by nurses.
 * Extends the Person class.
 */
public class Patient extends Person {
    private long pid; // Patient ID
    private LocalDate dateOfBirth; // Date of birth
    private String careLevel; // Care level
    private String roomnumber; // Room number
    private List<Treatment> allTreatments = new ArrayList<Treatment>(); // List of treatments

    /**
     * Constructs a Patient object with the given parameters.
     *
     * @param firstName     The first name of the patient.
     * @param surname       The surname of the patient.
     * @param dateOfBirth   The date of birth of the patient.
     * @param careLevel     The care level of the patient.
     * @param roomnumber    The room number of the patient.
     * @param archiveDate   The archive date of the patient.
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber, LocalDate archiveDate) {
        super(firstName, surname, archiveDate);
        this.dateOfBirth = dateOfBirth;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
    }

    /**
     * Constructs a Patient object with the given parameters.
     *
     * @param pid           The patient ID.
     * @param firstName     The first name of the patient.
     * @param surname       The surname of the patient.
     * @param dateOfBirth   The date of birth of the patient.
     * @param careLevel     The care level of the patient.
     * @param roomnumber    The room number of the patient.
     * @param archiveDate   The archive date of the patient.
     */
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber, LocalDate archiveDate) {
        super(firstName, surname, archiveDate);
        this.pid = pid;
        this.dateOfBirth = dateOfBirth;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
    }

    /**
     * Returns the patient ID.
     *
     * @return The patient ID.
     */
    public long getPid() {
        return pid;
    }

    /**
     * Returns the date of birth as a string.
     *
     * @return The date of birth as a string.
     */
    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }

    /**
     * Converts the given date of birth string to a LocalDate object and sets it as the new dateOfBirth.
     *
     * @param dateOfBirth The date of birth string in the format "YYYY-MM-DD".
     */
    public void setDateOfBirth(String dateOfBirth) {
        LocalDate birthday = DateConverter.convertStringToLocalDate(dateOfBirth);
        this.dateOfBirth = birthday;
    }

    /**
     * Returns the care level of the patient.
     *
     * @return The care level of the patient.
     */
    public String getCareLevel() {
        return careLevel;
    }

    /**
     * Sets the care level of the patient.
     *
     * @param careLevel The new care level.
     */
    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
    }

    /**
     * Returns the room number as a string.
     *
     * @return The room number as a string.
     */
    public String getRoomnumber() {
        return roomnumber;
    }

    /**
     * Sets the room number of the patient.
     *
     * @param roomnumber The new room number.
     */
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    /**
     * Adds a treatment to the list of treatments if it is not already present.
     *
     * @param treatment The treatment to be added.
     * @return True if the treatment was added successfully, false otherwise.
     */
    public boolean add(Treatment treatment) {
        if (!this.allTreatments.contains(treatment)) {
            this.allTreatments.add(treatment);
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the patient object.
     *
     * @return The string representation of the patient object.
     */
    @Override
    public String toString() {
        return "Patient" +
                "\nMNID: " + this.pid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.dateOfBirth +
                "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomnumber +
                "\nArchiveDate: " + this.getArchiveDate() +
                "\n";
    }
}