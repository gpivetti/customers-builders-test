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

@ControllerAdvice
public class DefaultErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                            final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http, errorMessage(ex));
        handleLogError(responseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                            final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.BAD_REQUEST, http, errorMessage(ex));
        handleLogError(responseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.METHOD_NOT_ALLOWED, http, errorMessage(ex));
        handleLogError(responseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }
}