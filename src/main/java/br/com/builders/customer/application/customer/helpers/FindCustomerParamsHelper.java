package br.com.builders.customer.application.customer.helpers;

import br.com.builders.customer.commons.dto.PageFiltersData;
import br.com.builders.customer.commons.enums.SortingEnum;
import br.com.builders.customer.domain.customer.dto.FindCustomersParamsDTO;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import org.apache.commons.lang3.StringUtils;

public class FindCustomerParamsHelper {
    public static FindCustomersParamsDTO generateParams(String filter, String sort, String limit, String offset)
            throws InvalidConstraintException {
        try {
            return FindCustomersParamsDTO.builder()
                    .filters(FieldsFilterProcessorHelper.handle(filter))
                    .pageFilters(normalizePageFilters(limit, offset))
                    .sorting(SortingEnum.of(sort))
                    .build();
        } catch (NumberFormatException ex) {
            throw new InvalidConstraintException("Invalid limit and offset params");
        }
    }

    public static PageFiltersData normalizePageFilters(String limit, String offset) {
        return (StringUtils.isNotEmpty(limit) || StringUtils.isNotEmpty(offset))
                ? PageFiltersData.builder()
                    .limit(StringUtils.isNotEmpty(limit) ? Long.parseLong(limit.trim()) : 0)
                    .offset(StringUtils.isNotEmpty(limit) ? Long.parseLong(limit.trim()) : 0)
                    .build()
                : null;
    }
}
