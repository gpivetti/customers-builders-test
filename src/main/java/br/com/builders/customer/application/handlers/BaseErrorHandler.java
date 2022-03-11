package br.com.builders.customer.application.handlers;

import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.domain.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public abstract class BaseErrorHandler {
    @Autowired
    protected LogService logService;

    protected ResponseEntity<ApiResponseErrorDTO> handleErrorResponse(@NotNull ApiResponseErrorDTO responseErrorDTO) {
        HttpStatus httpStatus = HttpStatus.resolve(responseErrorDTO.getStatus());
        return new ResponseEntity<>(responseErrorDTO, httpStatus != null
                ? httpStatus
                : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected void handleLogError(ApiResponseErrorDTO responseDTO) {
        if (responseDTO.getErrors() != null && !responseDTO.getErrors().isEmpty()) {
            this.logService.sendLogError(responseDTO.getErrors().get(0).getCode(),
                    responseDTO.getErrors().get(0).getMessage());
        } else {
            this.logService.sendLogError(responseDTO.getError(), responseDTO.getMessage());
        }
    }
}
