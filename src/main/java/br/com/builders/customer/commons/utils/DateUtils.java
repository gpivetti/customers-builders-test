package br.com.builders.customer.commons.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String normalizeCurrentTimestamp() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            ZonedDateTime now = ZonedDateTime.now();
            return dtf.format(now);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
