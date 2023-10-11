package org.example.event;

import org.example.ProfileImageEvent;

import java.time.Instant;

public class SuccessResult implements ProcessingResult{

    private ProfileImageEvent sourceEvent;
    private Instant processedAt;

    public SuccessResult(ProfileImageEvent sourceEvent) {
        this.sourceEvent = sourceEvent;
        this.processedAt = Instant.now();
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public ProfileImageEvent getSourceEvent() {
        return sourceEvent;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}
