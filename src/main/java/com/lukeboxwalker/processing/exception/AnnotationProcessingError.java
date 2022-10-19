package com.lukeboxwalker.processing.exception;

import java.io.Serial;

public class AnnotationProcessingError extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 19892738959823L;

    public AnnotationProcessingError() {
    }

    public AnnotationProcessingError(String message) {
        super(message);
    }

    public AnnotationProcessingError(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationProcessingError(Throwable cause) {
        super(cause);
    }

    public AnnotationProcessingError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
