package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Reader;

public class DateConverter {
    public static LocalDate convertStringToLocalDate(String date) {
        if (date == null) {
            return null;
        }
        String[] array = date.split("-");
        LocalDate result = LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
        return result;
    }

    public static LocalTime convertStringToLocalTime(String time) {
        String[] array = time.split(":");
        LocalTime result = LocalTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
        return result;
    }

}
