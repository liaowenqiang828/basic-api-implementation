package com.thoughtworks.rslist.Exception;

public class RsEventIndexInvalidException extends RuntimeException {
    private String errorMessage;

    public RsEventIndexInvalidException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
