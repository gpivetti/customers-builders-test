package br.com.builders.customer.commons.dto;

import br.com.builders.customer.commons.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseNotFoundDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private List<Filters> filters;

    @Data
    @AllArgsConstructor
    public static class Filters {
        private String name;
        private String value;
    }

    private ApiResponseNotFoundDTO() {}

    public static ApiResponseNotFoundDTO of() {
        return of(null);
    }

    public static ApiResponseNotFoundDTO of(String resource) {
        ApiResponseNotFoundDTO response = new ApiResponseNotFoundDTO();
        response.timestamp = DateUtils.normalizeCurrentDate();
        response.status = 404;
        response.error = "Not Found";
        if (StringUtils.isNotEmpty(resource)) {
            response.message = "Not Found " + resource;
        }
        return response;
    }

    public static ApiResponseNotFoundDTO of(String resource, Map<String, String> filters) {
        ApiResponseNotFoundDTO response = new ApiResponseNotFoundDTO();
        response.timestamp = DateUtils.normalizeCurrentDate();
        response.status = 404;
        response.error = "Not Found";
        if (StringUtils.isNotEmpty(resource)) {
            response.message = "Not Found " + resource;
        }
        if (filters != null && !filters.isEmpty()) {
            response.filters = filters.entrySet()
                    .stream()
                    .map(data -> new Filters(data.getKey(), data.getValue()))
                    .collect(Collectors.toList());
        }
        return response;
    }
}
