package org.example.event;

import org.example.ProfileImageEvent;

public interface ProcessingResult {
    boolean isSuccess();
    ProfileImageEvent getSourceEvent();
}
