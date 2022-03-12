package br.com.builders.customer.commons.dto;

import br.com.builders.customer.commons.enums.FilterEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldFilterData {
    private String field;
    private FilterEnum filter;
    private String value;
}
