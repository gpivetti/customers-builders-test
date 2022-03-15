package br.com.builders.customer.commons.dto;

import br.com.builders.customer.commons.enums.FilterEnum;
import lombok.Data;

@Data
public class FieldsDataDTO {
    private String field;
    private FilterEnum filter;
    private Object value;

    private FieldsDataDTO() {}

    public FieldsDataDTO(String field, FilterEnum filter, Object value) {
        this.field = field;
        this.filter = filter;
        this.value = value;
    }
}
