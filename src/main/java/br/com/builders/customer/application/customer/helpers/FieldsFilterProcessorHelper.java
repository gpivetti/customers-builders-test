package br.com.builders.customer.application.customer.helpers;

import br.com.builders.customer.commons.dto.FieldFilterData;
import br.com.builders.customer.commons.enums.FilterEnum;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FieldsFilterProcessorHelper {
    private static final String DEFAULT_ERROR_MESSAGE = "Invalid pattern of filters parameter";

    public static List<FieldFilterData> handle(String filter) throws InvalidConstraintException {
        if (filter == null) return new ArrayList<>();
        List<String> fieldFilters = Arrays.asList(filter.split(","));
        return fieldFilters.stream().map(fieldFilter -> {
            List<String> fieldsPositions = Arrays.asList(fieldFilter.split(":"));
            validateInvalidFieldsPositions(fieldsPositions);
            return FieldFilterData.builder()
                    .field(fieldsPositions.get(0))
                    .filter(getFilterOnPositionOfFields(fieldsPositions))
                    .value(fieldsPositions.get(fieldsPositions.size() - 1))
                    .build();
        }).collect(Collectors.toList());
    }

    private static FilterEnum getFilterOnPositionOfFields(List<String> fieldsPositions) {
        return fieldsPositions.size() == 2
                ? FilterEnum.EQUAL
                : FilterEnum.fromOperation(fieldsPositions.get(1));
    }

    private static void validateInvalidFieldsPositions(List<String> fieldsPositions) {
        if (fieldsPositions.size() <= 1 || fieldsPositions.size() > 3) {
            throw new InvalidConstraintException(DEFAULT_ERROR_MESSAGE);
        }
    }
}