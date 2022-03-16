package br.com.builders.customer.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidConstraintException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidConstraintException(String errorMessage) {
        super(errorMessage);
    }
}
