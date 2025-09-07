package com.example.membership_api.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    // Formatter for input format (slash separated)
    private static final DateTimeFormatter SLASH_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // Formatter for output format (DB standard)
    private static final DateTimeFormatter DB_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Parses a date string in the format yyyy/MM/dd and returns a LocalDateTime at start of day
     */

    
    public static LocalDateTime parseSlashDateToDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr, SLASH_DATE_FORMAT);
            return date.atStartOfDay();  // LocalDateTime at 00:00:00
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy/MM/dd");
        }
    }

    public static LocalDateTime[] calculateEndDateFromCurrent(int durationDays) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(durationDays);
        return new LocalDateTime[]{startDate, endDate};
    }

    /**
     * Formats a LocalDateTime into a String with format yyyy-MM-dd HH:mm:ss (for DB storage as string)
     */
    public static String formatDateTimeForDB(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DB_DATE_TIME_FORMAT);
    }
}
