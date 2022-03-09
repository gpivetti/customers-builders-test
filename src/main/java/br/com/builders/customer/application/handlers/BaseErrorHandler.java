package br.com.builders.customer.application.handlers;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseErrorHandler {
    protected Map<String, String> errorMessage(Exception ex) {
        return new HashMap<>(){{ put(getExceptionName(ex), getErrorMessageByException(ex)); }};
    }

    protected String getExceptionName(Exception ex) {
        return ex.getClass().getName();
    }

    protected String getErrorMessageByException(Exception ex) {
        return ex.getMessage() != null && !ex.getMessage().trim().equals("")
                ? ex.getMessage().split(":")[0]
                : "DefaultError";
    }
}
