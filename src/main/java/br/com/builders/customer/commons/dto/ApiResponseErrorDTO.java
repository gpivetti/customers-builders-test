package br.com.builders.customer.commons.dto;

import br.com.builders.customer.commons.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseErrorDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String method;
    private String path;
    private List<Errors> errors;

    @Data
    @AllArgsConstructor
    public static class Errors {
        private String code;
        private String message;
    }

    public static ApiResponseErrorDTO of(HttpStatus status, HttpServletRequest request, String errorMessage) {
        return of(status, request, errorMessage, null);
    }

    public static ApiResponseErrorDTO of(HttpStatus status, HttpServletRequest request, String errorMessage,
                                         List<Errors> errors) {
        return ApiResponseErrorDTO.builder()
                .timestamp(DateUtils.normalizeCurrentDate())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(errorMessage)
                .method(request.getMethod())
                .path(request.getRequestURI())
                .errors(errors)
                .build();
    }
}
