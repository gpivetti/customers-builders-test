package br.com.builders.customer.application.handlers;

import br.com.builders.customer.application.data.ApiResponseErrorDTO;
import br.com.builders.customer.main.exceptions.AppErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomErrorHandler extends BaseErrorHandler {
    @ExceptionHandler(value = { AppErrorException.class })
    @ResponseBody
    public ResponseEntity<ApiResponseErrorDTO> handleAppErrorException(final AppErrorException ex,
                                                                       final HttpServletRequest http) {
        ApiResponseErrorDTO responseDTO = ApiResponseErrorDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, http,
                errorMessage(ex));
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}