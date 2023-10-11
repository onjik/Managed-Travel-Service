package org.example.event;

import org.example.ProfileImageEvent;

public class FailureResult implements ProcessingResult{
    private ProfileImageEvent sourceEvent;
    private Throwable exception;
    private String message;

    public FailureResult(ProfileImageEvent sourceEvent, Throwable exception, String message) {
        this.sourceEvent = sourceEvent;
        this.exception = exception;
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public ProfileImageEvent getSourceEvent() {
        return null;
    }

    public Throwable getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }
}
