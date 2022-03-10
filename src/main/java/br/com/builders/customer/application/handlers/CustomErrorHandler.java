package br.com.builders.customer.application.handlers;

import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(value = { AppErrorException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleAppErrorException(final AppErrorException ex,
                                                                       final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, http,
                errorMessage(ex));
        handleLogError(responseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleResourceNotFoundException(final ResourceNotFoundException ex,
                                                                               final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.NOT_FOUND, http,
                this.normalizeResourceNotFoundErrors(ex));
        handleLogError(responseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    private Map<String, String> normalizeResourceNotFoundErrors(ResourceNotFoundException ex) {
        return this.checkValidResourceNotFoundErrorException(ex)
                ? new HashMap<>(){{
                    put(ex.resource(), ex.filters().entrySet().stream()
                            .map(data -> data.getKey() + "=" + data.getValue())
                            .collect(Collectors.joining(",")));
                    }}
                : new HashMap<>(){{ put(ex.getMessage(), ex.resource()); }};
    }

    private boolean checkValidResourceNotFoundErrorException(ResourceNotFoundException ex) {
        return ex.filters() != null && !ex.filters().isEmpty() && StringUtils.isNotEmpty(ex.resource());
    }
}