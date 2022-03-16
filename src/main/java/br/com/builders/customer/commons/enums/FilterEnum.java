package br.com.builders.customer.commons.enums;

import br.com.builders.customer.main.exceptions.InvalidConstraintException;

import java.util.HashMap;
import java.util.Map;

public enum FilterEnum {
    EQUAL,
    NOT_EQUAL,
    LIKE,
    LESS_THAN,
    LESS_THEN_EQUAL,
    GREATER_THAN,
    GREATER_THEN_EQUALS;

    public enum Operation {
        eq, ne, like, lt, lte, gt, gte;
    }

    private static final Map<Operation, FilterEnum> filtersMap = new HashMap<>() {{
        put(Operation.eq, EQUAL);
        put(Operation.ne, NOT_EQUAL);
        put(Operation.like, LIKE);
        put(Operation.lt, LESS_THAN);
        put(Operation.lte, LESS_THEN_EQUAL);
        put(Operation.gt, GREATER_THAN);
        put(Operation.gte, GREATER_THEN_EQUALS);
    }};

    public static FilterEnum fromOperation(String filter) throws InvalidConstraintException {
        try {
            return fromOperation(Operation.valueOf(filter.toLowerCase()));
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidConstraintException("invalid filter " + (filter == null ? "null" : filter));
        }
    }

    public static FilterEnum fromOperation(Operation filter) throws InvalidConstraintException {
        FilterEnum filterEnum = filtersMap.get(filter);
        if (filter == null || filterEnum == null) {
            throw new InvalidConstraintException("invalid filter " + (filter == null ? "null" : filter.name()));
        }
        return filterEnum;
    }
}
