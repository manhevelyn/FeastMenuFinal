package util; // Class tiện ích

// Class kiểm tra đầu vào ( Theo validate rule?)
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Validation {

    public static boolean isValidCustomerCode(String value) {
        return value != null && value.matches("^[cCgGkK]\\d{4}$");
    }

    public static boolean isValidName(String value) {
        if (value == null) {
            return false;
        }
        return value.length() >= 2 && value.length() <= 25;
    }

    // 032 -> 039, 052 056 058 059, 0706->079,081->086 088 089, 090 -> 099  
    public static boolean isValidPhone(String value) {
        if (value == null) {
            return false;
        }
        return value.matches(
                "^0[235789][\\d]{8}$");
        //"^03[2-9]|05[2689]|07[06-9]|08[1-6]|08[89]|09[0-9]\\d{7}$");
    }
    // any@any.any

    public static boolean isValidEmail(String value) {
        if (value == null) {
            return false;
        }
        // return value.matches("^[a-zA Z0-9._%+-]+@[a-zA Z0-9.-]+\\/[a-zA-Z]{2,}$");
        return value.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    public static LocalDate isValidDate(String value) {
        try {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (date.isAfter(LocalDate.now())) {
                return date;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
