package model;

import utils.DateConverter;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a treatment performed on a patient by a caregiver.
 */
public class Treatment {
    private long tid; // Treatment ID
    private long pid; // Patient ID
    private long cid; // Caregiver ID
    private LocalDate date; // Date of the treatment
    private LocalTime begin; // Start time of the treatment
    private LocalTime end; // End time of the treatment
    private String description; // Description of the treatment
    private String remarks; // Remarks or notes about the treatment
    private LocalDate archiveDate; // Archive date of the treatment

    /**
     * Constructs a Treatment object with the given parameters.
     *
     * @param pid           The ID of the patient receiving the treatment.
     * @param cid           The ID of the caregiver performing the treatment.
     * @param date          The date of the treatment.
     * @param begin         The start time of the treatment.
     * @param end           The end time of the treatment.
     * @param description   The description of the treatment.
     * @param remarks       Any remarks or notes about the treatment.
     * @param archiveDate   The archive date of the treatment.
     */
    public Treatment(long pid, long cid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, LocalDate archiveDate) {
        this.pid = pid;
        this.cid = cid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.archiveDate = archiveDate;
    }

    /**
     * Constructs a Treatment object with the given parameters.
     *
     * @param tid           The ID of the treatment.
     * @param pid           The ID of the patient receiving the treatment.
     * @param cid           The ID of the caregiver performing the treatment.
     * @param date          The date of the treatment.
     * @param begin         The start time of the treatment.
     * @param end           The end time of the treatment.
     * @param description   The description of the treatment.
     * @param remarks       Any remarks or notes about the treatment.
     * @param archiveDate   The archive date of the treatment.
     */
    public Treatment(long tid, long pid, long cid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, LocalDate archiveDate) {
        this.tid = tid;
        this.pid = pid;
        this.cid = cid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.archiveDate = archiveDate;
    }

    /**
     * Returns the ID of the treatment.
     *
     * @return The ID of the treatment.
     */
    public long getTid() {
        return tid;
    }

    /**
     * Returns the ID of the patient receiving the treatment.
     *
     * @return The ID of the patient.
     */
    public long getPid() {
        return this.pid;
    }

    /**
     * Returns the ID of the caregiver performing the treatment.
     *
     * @return The ID of the caregiver.
     */
    public long getCid() {
        return this.cid;
    }

    /**
     * Returns the date of the treatment as a string.
     *
     * @return The date of the treatment in the format "YYYY-MM-DD".
     */
    public String getDate() {
        return date.toString();
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
     * Returns the start time of the treatment as a string.
     *
     * @return The start time of the treatment in the format "HH:mm:ss".
     */
    public String getBegin() {
        return begin.toString();
    }

    /**
     * Returns the end time of the treatment as a string.
     *
     * @return The end time of the treatment in the format "HH:mm:ss".
     */
    public String getEnd() {
        return end.toString();
    }

    /**
     * Sets the date of the treatment using the given string representation.
     *
     * @param s_date The date of the treatment as a string in the format "YYYY-MM-DD".
     */
    public void setDate(String s_date) {
        LocalDate date = DateConverter.convertStringToLocalDate(s_date);
        this.date = date;
    }

    /**
     * Sets the archive date of the treatment using the given string representation.
     *
     * @param s_date The archive date of the treatment as a string in the format "YYYY-MM-DD".
     */
    public void setArchiveDate(String s_date) {
        LocalDate archiveDate = DateConverter.convertStringToLocalDate(s_date);
        this.archiveDate = archiveDate;
    }

    /**
     * Sets the start time of the treatment using the given string representation.
     *
     * @param begin The start time of the treatment as a string in the format "HH:mm:ss".
     */
    public void setBegin(String begin) {
        LocalTime time = DateConverter.convertStringToLocalTime(begin);
        this.begin = time;
    }

    /**
     * Sets the end time of the treatment using the given string representation.
     *
     * @param end The end time of the treatment as a string in the format "HH:mm:ss".
     */
    public void setEnd(String end) {
        LocalTime time = DateConverter.convertStringToLocalTime(end);
        this.end = time;
    }

    /**
     * Returns the description of the treatment.
     *
     * @return The description of the treatment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the treatment.
     *
     * @param description The new description of the treatment.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the remarks or notes about the treatment.
     *
     * @return The remarks or notes about the treatment.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the remarks or notes about the treatment.
     *
     * @param remarks The new remarks or notes about the treatment.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Returns a string representation of the treatment.
     *
     * @return A string representation of the treatment.
     */
    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid +
                "\nPID: " + this.pid +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nDescription: " + this.description +
                "\nRemarks: " + this.remarks +
                "\nArchiveDate: " + this.archiveDate + "\n";
    }
}