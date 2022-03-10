package br.com.builders.customer.application.handlers;

import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.domain.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseErrorHandler {
    @Autowired
    protected LogService logService;

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

    protected void handleLogError(ApiResponseErrorDTO responseDTO) {
        this.logService.sendLogError(responseDTO.getErrors().get(0).getCode(),
                responseDTO.getErrors().get(0).getMessage());
    }
}
