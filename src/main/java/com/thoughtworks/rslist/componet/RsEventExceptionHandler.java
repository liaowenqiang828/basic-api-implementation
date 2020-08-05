package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.Exception.Error;
import com.thoughtworks.rslist.Exception.RsEventIndexInvalidException;
import com.thoughtworks.rslist.Exception.RsEventRequestParamException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {
    @ExceptionHandler({RsEventIndexInvalidException.class, MethodArgumentNotValidException.class, RsEventRequestParamException.class})
    public ResponseEntity RsEventIndexInvalidExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException) {
            if (e.getMessage().contains("Field error in object 'rsEvent'")) {
                errorMessage = "invalid param";
            } else {
                errorMessage = "invalid user";
            }
        } else if (e instanceof RsEventRequestParamException) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = e.getMessage();
        }

        Error error = new Error();
        error.setError(errorMessage);

        return ResponseEntity.badRequest().body(error);
    }
}
