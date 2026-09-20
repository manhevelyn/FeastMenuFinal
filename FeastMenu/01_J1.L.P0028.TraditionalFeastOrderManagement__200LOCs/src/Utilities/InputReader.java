package Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputReader {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private final Scanner scanner;

    public InputReader() {
        scanner = new Scanner(System.in);
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            String input = readString(prompt);
            try {
                int number = Integer.parseInt(input);
                if (number > 0) return number;
            } catch (NumberFormatException exception) {
                // The error message is displayed below.
            }
            System.out.println("Please enter an integer greater than zero.");
        }
    }

    public Integer readOptionalPositiveInt(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) return null;
            try {
                int number = Integer.parseInt(input);
                if (number > 0) return number;
            } catch (NumberFormatException exception) {
                // The error message is displayed below.
            }
            System.out.println("Please enter an integer greater than zero, or leave blank.");
        }
    }

    public LocalDate readFutureDate(String prompt, boolean optional) {
        while (true) {
            String input = readString(prompt);
            if (optional && input.isEmpty()) return null;
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMAT);
                if (Validator.isFutureDate(date)) return date;
            } catch (DateTimeParseException exception) {
                // The error message is displayed below.
            }
            System.out.println("Date must follow dd/MM/yyyy and be in the future.");
        }
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (input.equalsIgnoreCase("Y")) return true;
            if (input.equalsIgnoreCase("N")) return false;
            System.out.println("Please enter Y or N.");
        }
    }
}
