package br.com.builders.customer.commons.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String normalizeCurrentDate() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime now = ZonedDateTime.now();
            return dtf.format(now);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
