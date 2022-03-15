package br.com.builders.customer.controllers.handlers;

import br.com.builders.customer.controllers.dto.ApiResponseErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DefaultErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseErrorDTO> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                            final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http,
                ex.getClass().getSimpleName(), this.normalizeArgumentNotValidErrors(ex));
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseErrorDTO> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                            final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http, ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseErrorDTO> handleHttpMediaTypeNotSupportedException(
            final HttpMediaTypeNotSupportedException ex, final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http, ex.getMessage());
        handleLogError(responseDTO);
        return this.handleErrorResponse(responseDTO);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
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