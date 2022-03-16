package br.com.builders.customer.controllers.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GenericPaginatedResponseDTO<T> {
    private int currentPage;
    private int currentSize;
    private T payload;
    private List<Link> links;

    private enum LinkRel {
        prev, next;
    }

    @Data
    private static class Link {
        private LinkRel rel;
        private String href;

        public Link(LinkRel rel, String href) {
            this.rel = rel;
            this.href = href;
        }
    }

    private GenericPaginatedResponseDTO() {}

    public GenericPaginatedResponseDTO(T responsePayload, String uri, List<String> params, Pageable pageParams) {
        this.currentPage = pageParams.getPageNumber();
        this.currentSize = pageParams.getPageSize();
        this.payload = responsePayload;
        this.links = new ArrayList<>();
        if (pageParams.getPageNumber() > 0) {
            this.links.add(new Link(LinkRel.prev, this.normalizeUrlQueryString(uri, params, pageParams, LinkRel.prev)));
        }
        this.links.add(new Link(LinkRel.next, this.normalizeUrlQueryString(uri, params, pageParams, LinkRel.next)));
    }

    private String normalizeUrlQueryString(String uri, List<String> params, Pageable pageParams, LinkRel linkRel) {
        String realUrl = (uri != null ? uri : "") + "?size=" + pageParams.getPageSize();
        realUrl += "&page=" + (linkRel == LinkRel.prev
                ? (pageParams.getPageNumber() == 1 ? 0 : pageParams.getPageNumber() - 1)
                : pageParams.getPageNumber() + 1);
        if (params != null && !params.isEmpty()) {
            realUrl += "&" + String.join("&", params);
        }
        if (!pageParams.getSort().isEmpty()) {
            realUrl += "&sort=" + pageParams.getSort().stream()
                    .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
                    .collect(Collectors.joining("&sort="));
        }
        return realUrl;
    }
}
