package com.thoughtworks.rslist.Exception;

public class RsEventRequestParamException extends RuntimeException {
    private String errorMessage;

    public RsEventRequestParamException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
