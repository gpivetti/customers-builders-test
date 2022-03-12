package br.com.builders.customer.application.handlers;

import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.domain.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseErrorHandler {
    @Autowired
    protected LogService logService;

    protected ResponseEntity<ApiResponseErrorDTO> handleErrorResponse(ApiResponseErrorDTO responseErrorDTO) {
        HttpStatus httpStatus = responseErrorDTO != null ? HttpStatus.resolve(responseErrorDTO.getStatus()) : null;
        return new ResponseEntity<>(responseErrorDTO, httpStatus != null
                ? httpStatus
                : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected void handleLogError(ApiResponseErrorDTO responseDTO) {
        if (responseDTO == null) return;
        if (responseDTO.getErrors() != null && !responseDTO.getErrors().isEmpty()) {
            this.logService.sendLogError(responseDTO.getErrors().get(0).getCode(),
                    responseDTO.getErrors().get(0).getMessage());
        } else {
            this.logService.sendLogError(responseDTO.getError(), responseDTO.getMessage());
        }
    }
}
