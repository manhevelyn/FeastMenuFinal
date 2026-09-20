package Utilities;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Validator {
    private static final Pattern CUSTOMER_CODE =
            Pattern.compile("^[CGK]\\d{4}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern VIETNAMESE_PHONE =
            Pattern.compile("^(03[2-9]|05[2689]|07[06-9]|08[1-9]|09[0-9])\\d{7}$");

    private Validator() {}

    public static boolean isCustomerCode(String value) {
        return value != null && CUSTOMER_CODE.matcher(value.trim()).matches();
    }

    public static boolean isName(String value) {
        int length = value == null ? 0 : value.trim().length();
        return length >= 2 && length <= 25;
    }

    public static boolean isPhoneNumber(String value) {
        return value != null && VIETNAMESE_PHONE.matcher(value.trim()).matches();
    }

    public static boolean isEmail(String value) {
        return value != null && EMAIL.matcher(value.trim()).matches();
    }

    public static boolean isFutureDate(LocalDate value) {
        return value != null && value.isAfter(LocalDate.now());
    }
}
