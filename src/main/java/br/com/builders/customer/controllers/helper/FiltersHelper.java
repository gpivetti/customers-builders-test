package br.com.builders.customer.controllers.helper;

import br.com.builders.customer.commons.dto.FilterDataDTO;
import br.com.builders.customer.commons.enums.FilterEnum;
import br.com.builders.customer.main.exceptions.InvalidParameterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FiltersHelper {
    public static List<FilterDataDTO> buildFilterList(List<String> filters) throws InvalidParameterException {
        try {
            if (filters == null || filters.isEmpty()) return new ArrayList<>();
            return filters.stream().map(filter -> {
                List<String> filterPositions = Arrays.asList(filter.split(":"));
                validateInvalidFilterPositions(filterPositions);
                return new FilterDataDTO(filterPositions.get(0),
                        getFilterOperationOnPositions(filterPositions),
                        filterPositions.get(filterPositions.size() - 1));
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

    public static List<String> buildQueryParametersFromStringFilter(List<String> filters) {
        return filters != null && !filters.isEmpty()
                ? filters.stream().map(filter -> "filter="+filter).collect(Collectors.toList())
                : null;
    }
}
