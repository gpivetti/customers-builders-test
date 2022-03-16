package br.com.builders.customer.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MESSAGE = "ObjectValidationException";

    private final String entity;
    private final Map<String, String> validationMap;

    public ObjectValidationException(String entity) {
        super(DEFAULT_MESSAGE);
        this.entity = entity;
        this.validationMap = new HashMap<>();
    }

    public ObjectValidationException(Map<String, String> validatorMap) {
        super(DEFAULT_MESSAGE);
        this.entity = null;
        this.validationMap = validatorMap;
    }

    public ObjectValidationException(String entity, Map<String, String> validatorMap) {
        super(DEFAULT_MESSAGE);
        this.entity = entity;
        this.validationMap = validatorMap;
    }

    public String entity() {
        return this.entity;
    }

    public Map<String, String> validationMap() {
        return this.validationMap;
    }
}
