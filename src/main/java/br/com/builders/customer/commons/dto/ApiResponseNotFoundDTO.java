package br.com.builders.customer.commons.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ApiResponseNotFoundDTO {
    private String processedAt;
    private int status;
    private String message;

    private ApiResponseNotFoundDTO() {}

    public static ApiResponseNotFoundDTO of() {
        return of(null);
    }

    public static ApiResponseNotFoundDTO of(String resource) {
        ApiResponseNotFoundDTO response = new ApiResponseNotFoundDTO();
        response.processedAt = normalizeCurrentDate();
        response.status = 404;
        response.message = "Not Found" + (StringUtils.isNotEmpty(resource) ? " " + resource : "");
        return response;
    }

    private static String normalizeCurrentDate() {
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
