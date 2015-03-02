package com.google.places.showcase.event;

/**
 * Failed request information container
 */
public class ApiErrorEvent {
    private Exception mException;
    private String mErrorMessage;

    public ApiErrorEvent(Exception exception) {
        mException = exception;
    }

    public ApiErrorEvent(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return mException != null ? mException.getMessage() : mErrorMessage;
    }
}
