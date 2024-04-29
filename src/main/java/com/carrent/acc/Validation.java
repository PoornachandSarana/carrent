package com.carrent.acc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;


public class Validation {
    public static boolean validateFromDate(String dateString) {
        // Regular expression to match "yyyy-mm-dd" format
        String dateFormatRegex = "\\d{4}-\\d{2}-\\d{2}";

        // Check if the dateString matches the desired format
        if (!Pattern.matches(dateFormatRegex, dateString)) {
            return false;
        }

        // Parse the dateString into LocalDate
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return false; // Date parsing failed
        }

        // Get tomorrow's date
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // Check if the date is not before tomorrow
        return !date.isBefore(tomorrow);
    }

    public static boolean validateRentLength(String userInput) {
        String cleanedInput = userInput.replace("days", "").trim();
        return cleanedInput.matches("[1-9]|[12]\\d|30");
    }
}
