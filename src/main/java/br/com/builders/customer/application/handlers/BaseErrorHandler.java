package br.com.builders.customer.application.handlers;

import br.com.builders.customer.application.dto.ApiResponseErrorDTO;
import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpHeaders;

import java.util.stream.Collectors;

public abstract class BaseErrorHandler {
    @Autowired
    protected LogService logService;

    protected ResponseEntity<ApiResponseErrorDTO> handleErrorResponse(ApiResponseErrorDTO responseErrorDTO) {
        HttpStatus httpStatus = responseErrorDTO != null ? HttpStatus.resolve(responseErrorDTO.getStatus()) : null;
        return new ResponseEntity<>(responseErrorDTO,
                this.getDefaultHeaders(),
                httpStatus != null
                ? httpStatus
                : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<ApiResponseNotFoundDTO> handleErrorResponse(ApiResponseNotFoundDTO responseNotFoundDTO) {
        return new ResponseEntity<>(responseNotFoundDTO, this.getDefaultHeaders(), HttpStatus.NOT_FOUND);
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
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

    protected void handleLogError(ApiResponseNotFoundDTO responseNotFoundDTO) {
        if (responseNotFoundDTO == null) return;
        String message = responseNotFoundDTO.getFilters() != null && !responseNotFoundDTO.getFilters().isEmpty()
                ? responseNotFoundDTO.getFilters().stream()
                    .map(filter -> filter.getName()+"="+filter.getValue())
                    .collect(Collectors.joining(","))
                : (responseNotFoundDTO.getMessage() != null
                    ? responseNotFoundDTO.getMessage()
                    : responseNotFoundDTO.getError());
        this.logService.sendLogError(responseNotFoundDTO.getError(), message);
    }
}
