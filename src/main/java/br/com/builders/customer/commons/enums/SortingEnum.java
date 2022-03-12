package br.com.builders.customer.commons.enums;

import br.com.builders.customer.main.exceptions.InvalidConstraintException;

public enum SortingEnum {
    ASC,
    DESC;

    public static SortingEnum of(String sorting) throws InvalidConstraintException {
        try {
            return SortingEnum.valueOf(sorting.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidConstraintException("invalid sorting " + (sorting == null ? "null" : sorting));
        }
    }
}
