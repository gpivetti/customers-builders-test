package br.com.builders.customer.domain.customer.dto;

import br.com.builders.customer.commons.dto.FieldFilterData;
import br.com.builders.customer.commons.dto.PageFiltersData;
import br.com.builders.customer.commons.enums.SortingEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FindCustomersParamsDTO {
    private List<FieldFilterData> filters;
    private PageFiltersData pageFilters;
    private SortingEnum sorting;
}
