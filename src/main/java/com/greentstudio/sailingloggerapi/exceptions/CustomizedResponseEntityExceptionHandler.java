package com.greentstudio.sailingloggerapi.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * This class provides custom exception handling for all exceptions.
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Overrides the method in ResponseEntityExceptionHandler class, providing a more appropriate exception handler.
     *
     * @param ex      The thrown exception.
     * @param request The request that threw the exception.
     * @return Returns the exception structure and a 500 status.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Specific Exception Handler for non-existing Boats which returns a 404 status code.
     *
     * @param ex      The thrown exception.
     * @param request The request that threw the exception.
     * @return Returns the exception structure and a 404 status.
     */
    @ExceptionHandler(BoatNotFoundException.class)
    public final ResponseEntity<Object> handleBoatNotFoundExceptions(BoatNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles invalid creation parameters given to POST.
     *
     * @param ex      The triggering exception.
     * @param headers The headers to be returned to the client.
     * @param status  The Status code to be returned to the client.
     * @param request The incorrect request.
     * @return Returns the exception structure and a 400 Status Code
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), "Validation Failed", ex.getBindingResult().toString());
        //returning exception structure and specific status
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
