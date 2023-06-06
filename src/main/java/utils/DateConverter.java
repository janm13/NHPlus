package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Reader;

/**
 * The `DateConverter` class provides utility methods for converting string representations of dates and times
 * to their corresponding `LocalDate` and `LocalTime` objects.
 */
public class DateConverter {
    /**
     * Converts a string representation of a date in the format "YYYY-MM-DD" to a `LocalDate` object.
     *
     * @param date The string representation of the date.
     * @return The converted `LocalDate` object, or `null` if the input is `null`.
     */
    public static LocalDate convertStringToLocalDate(String date) {
        if (date == null) {
            return null;
        }
        String[] array = date.split("-");
        LocalDate result = LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
        return result;
    }

    /**
     * Converts a string representation of a time in the format "HH:MM" to a `LocalTime` object.
     *
     * @param time The string representation of the time.
     * @return The converted `LocalTime` object.
     */
    public static LocalTime convertStringToLocalTime(String time) {
        String[] array = time.split(":");
        LocalTime result = LocalTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
        return result;
    }
}
