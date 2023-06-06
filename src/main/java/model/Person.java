package model;

import utils.DateConverter;

import java.time.LocalDate;

public abstract class Person {
    private String firstName;
    private String surname;
    private LocalDate archiveDate;

    public Person(String firstName, String surname, LocalDate archiveDate) {
        this.firstName = firstName;
        this.surname = surname;
        this.archiveDate = archiveDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getArchiveDate() {
        if (archiveDate == null) {
            return null;
        }
        else {
            return archiveDate.toString();
        }
    }

    public void setArchiveDate(String s_date) {
        LocalDate archiveDate = DateConverter.convertStringToLocalDate(s_date);
        this.archiveDate = archiveDate;
    }
}
