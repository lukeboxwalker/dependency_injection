package com.lukeboxwalker.processing.exception;

import java.io.Serial;

public class DependencyLookUpException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 19356738959823L;

    public DependencyLookUpException(String message) {
        super(message);
    }

    public DependencyLookUpException(Throwable cause) {
        super(cause);
    }
}
