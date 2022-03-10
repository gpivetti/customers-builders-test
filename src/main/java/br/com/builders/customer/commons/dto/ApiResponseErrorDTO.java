package br.com.builders.customer.commons.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class ApiResponseErrorDTO {
    private String processedAt;
    private int status;
    private String message;
    private String method;
    private String path;
    private List<Errors> errors;

    @Data
    @Builder
    public static class Errors {
        private String code;
        private String message;
    }

    public static ApiResponseErrorDTO of(HttpStatus status, HttpServletRequest request, String errorMessage) {
        return of(status, request, new HashMap<>(){{ put("Error", errorMessage); }});
    }

    public static ApiResponseErrorDTO of(HttpStatus status, HttpServletRequest request, Map<String, String> errors) {
        return ApiResponseErrorDTO.builder()
                .processedAt(normalizeCurrentDate())
                .status(status.value())
                .message(status.getReasonPhrase())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .errors(errors.entrySet().stream()
                        .map(error ->
                                ApiResponseErrorDTO.Errors.builder()
                                        .code(error.getKey())
                                        .message(error.getValue())
                                        .build()
                        ).collect(Collectors.toList()))
                .build();
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
