package br.com.builders.customer.commons.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageFiltersData {
    public long limit;
    public long offset;
}
