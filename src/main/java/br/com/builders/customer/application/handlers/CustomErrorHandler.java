package br.com.builders.customer.application.handlers;

import br.com.builders.customer.application.dto.ApiResponseErrorDTO;
import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.main.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(AppErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponseErrorDTO> handleAppErrorException(final AppErrorException ex,
                                                                       final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, http,
                ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(InvalidConstraintException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ApiResponseErrorDTO> handleInvalidConstraintException(final InvalidConstraintException ex,
                                                                                final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.UNPROCESSABLE_ENTITY, http,
                ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseErrorDTO> handleInvalidParameterException(final InvalidParameterException ex,
                                                                               final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.UNPROCESSABLE_ENTITY, http,
                ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(ObjectValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseErrorDTO> handleObjectValidationException(final ObjectValidationException ex,
                                                                               final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http,
                this.normalizeObjectValidationErrorMessage(ex), this.normalizeObjectValidationErrors(ex));
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponseNotFoundDTO> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        ApiResponseNotFoundDTO responseDTO = ApiResponseNotFoundDTO.of(ex.resource(), ex.filters());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    private boolean checkValidResourceNotFoundErrorException(ResourceNotFoundException ex) {
        return ex.filters() != null && !ex.filters().isEmpty() && StringUtils.isNotEmpty(ex.resource());
    }

    private String normalizeObjectValidationErrorMessage(ObjectValidationException ex) {
        return StringUtils.isNotEmpty(ex.entity())
                ? "Invalid " + ex.entity()
                : ex.getMessage();
    }

    private List<ApiResponseErrorDTO.Errors> normalizeObjectValidationErrors(ObjectValidationException ex) {
        return ex.validationMap() != null && !ex.validationMap().isEmpty()
                ? ex.validationMap().entrySet().stream()
                    .map(filter -> new ApiResponseErrorDTO.Errors(filter.getKey(), filter.getValue()))
                    .collect(Collectors.toList())
                : null;
    }
}