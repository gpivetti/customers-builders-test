package br.com.builders.customer.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppErrorException(String errorMessage) {
        super(errorMessage);
    }

    public AppErrorException(Exception ex) {
        super(ex);
    }
}
