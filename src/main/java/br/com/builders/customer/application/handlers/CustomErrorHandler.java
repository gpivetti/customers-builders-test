package br.com.builders.customer.application.handlers;

import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.commons.enums.FilterEnum;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import br.com.builders.customer.main.exceptions.ObjectValidationException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(value = { AppErrorException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleAppErrorException(final AppErrorException ex,
                                                                       final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, http,
                ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(value = { InvalidConstraintException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleInvalidConstraintException(final InvalidConstraintException ex,
                                                                                final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.UNPROCESSABLE_ENTITY, http,
                ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleResourceNotFoundException(final ResourceNotFoundException ex,
                                                                               final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.NOT_FOUND, http,
                this.normalizeResourceNotFoundErrorMessage(ex), this.normalizeResourceNotFoundErrors(ex));
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(value = { ObjectValidationException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleObjectValidationException(final ObjectValidationException ex,
                                                                               final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http,
                this.normalizeObjectValidationErrorMessage(ex), this.normalizeObjectValidationErrors(ex));
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    private String normalizeResourceNotFoundErrorMessage(ResourceNotFoundException ex) {
        return StringUtils.isNotEmpty(ex.resource())
                ? ex.resource() + " Not Found"
                : ex.getMessage();
    }

    private List<ApiResponseErrorDTO.Errors> normalizeResourceNotFoundErrors(ResourceNotFoundException ex) {
        return this.checkValidResourceNotFoundErrorException(ex)
                ? ex.filters().entrySet().stream()
                    .map(filter -> new ApiResponseErrorDTO.Errors(filter.getKey(), filter.getValue()))
                    .collect(Collectors.toList())
                : null;
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