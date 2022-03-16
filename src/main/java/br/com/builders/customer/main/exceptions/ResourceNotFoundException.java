package br.com.builders.customer.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MESSAGE = "ResourceNotFoundException";

    private final String resource;
    private final Map<String, String> filters;

    public ResourceNotFoundException() {
        super(DEFAULT_MESSAGE);
        this.resource = null;
        this.filters = null;
    }

    public ResourceNotFoundException(String resource) {
        super(DEFAULT_MESSAGE);
        this.resource = resource;
        this.filters = null;
    }

    public ResourceNotFoundException(String resource, Map<String, String> filters) {
        super(DEFAULT_MESSAGE);
        this.resource = resource;
        this.filters = filters;
    }

    public String resource() {
        return this.resource;
    }

    public Map<String, String> filters() {
        return this.filters;
    }
}
