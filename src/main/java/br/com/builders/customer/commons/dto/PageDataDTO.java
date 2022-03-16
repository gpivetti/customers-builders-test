package br.com.builders.customer.commons.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class PageDataDTO {
    public int page;
    public int size;

    private PageDataDTO() {}

    public PageDataDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public static PageDataDTO fromPageable(Pageable pageable) {
        return new PageDataDTO(pageable.getPageNumber(), pageable.getPageSize());
    }
}
