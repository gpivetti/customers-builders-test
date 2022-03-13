package br.com.builders.customer.commons.dto;

import br.com.builders.customer.commons.enums.SortingEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SortFieldDataDTO {
    public String field;
    public SortingEnum sort;
}
