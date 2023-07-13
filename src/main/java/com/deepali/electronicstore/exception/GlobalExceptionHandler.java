package com.deepali.electronicstore.exception;

import com.deepali.electronicstore.paylods.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private  Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //handler resource not found exception

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {
        logger.info("Exception Handler invoked");
        ApiResponseMessage response = ApiResponseMessage
                                    .builder()
                                    .message(ex.getMessage())
                                    .status(HttpStatus.NOT_FOUND)
                                    .success(true).build();

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    //handle Bad Request Api Exception
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequest ex)
    {
        logger.info("Bad Api Request");
        ApiResponseMessage response = ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false).build();

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
