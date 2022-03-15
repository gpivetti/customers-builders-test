package br.com.builders.customer.commons.dto;

import br.com.builders.customer.commons.enums.FilterEnum;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FiltersDataFieldsDTO {
    private String field;
    private FilterEnum filter;
    private Object value;

    public static List<FiltersDataFieldsDTO> fromStringFilters(List<String> filters) throws InvalidParameterException {
        try {
            if (filters == null || filters.isEmpty()) return new ArrayList<>();
            return filters.stream().map(filter -> {
                List<String> filterPositions = Arrays.asList(filter.split(":"));
                validateInvalidFilterPositions(filterPositions);
                return FiltersDataFieldsDTO.builder()
                        .field(filterPositions.get(0))
                        .filter(getFilterOperationOnPositions(filterPositions))
                        .value(filterPositions.get(filterPositions.size() - 1))
                        .build();
            }).collect(Collectors.toList());
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidParameterException("Error parsing string list filter ["+  ex.getMessage() + "]");
        }
    }

    private static FilterEnum getFilterOperationOnPositions(List<String> filterPositions) {
        return filterPositions.size() == 2
                ? FilterEnum.EQUAL
                : FilterEnum.fromOperation(filterPositions.get(1));
    }

    private static void validateInvalidFilterPositions(List<String> filterPositions) {
        if (filterPositions.size() <= 1 || filterPositions.size() > 3) {
            throw new InvalidParameterException("Invalid pattern of filters parameters");
        }
    }
}
