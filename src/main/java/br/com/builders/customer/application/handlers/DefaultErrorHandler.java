package br.com.builders.customer.application.handlers;

import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                            final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http,
                ex.getClass().getSimpleName(), this.normalizeArgumentNotValidErrors(ex));
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                            final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http, ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.METHOD_NOT_ALLOWED, http, ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    private List<ApiResponseErrorDTO.Errors> normalizeArgumentNotValidErrors(MethodArgumentNotValidException ex) {
        return this.checkArgumentNotValidErrors(ex)
                ? ex.getBindingResult().getAllErrors().stream()
                    .map(error -> new ApiResponseErrorDTO.Errors(error.getCode(), error.getDefaultMessage()))
                    .collect(Collectors.toList())
                : null;
    }

    private boolean checkArgumentNotValidErrors(MethodArgumentNotValidException ex) {
        ex.getBindingResult();
        ex.getBindingResult().getAllErrors();
        return !ex.getBindingResult().getAllErrors().isEmpty();
    }
}