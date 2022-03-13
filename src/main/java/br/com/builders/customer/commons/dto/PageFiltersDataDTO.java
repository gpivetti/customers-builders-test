package br.com.builders.customer.commons.dto;

import lombok.Data;

@Data
public class PageFiltersDataDTO {
    public int page;
    public int size;

    private PageFiltersDataDTO() {}

    public PageFiltersDataDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
