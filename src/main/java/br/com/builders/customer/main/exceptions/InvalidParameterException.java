package br.com.builders.customer.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidParameterException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidParameterException(Exception ex) {
        super(ex);
    }
}