package br.com.builders.customer.commons.dto;

import lombok.Data;

@Data
public class PageDataDTO {
    public int page;
    public int size;

    private PageDataDTO() {}

    public PageDataDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
