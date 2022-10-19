package com.lukeboxwalker.processing.exception;

import java.io.Serial;

public class ObjectCreationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 19892738959823L;

    public ObjectCreationException(String message) {
        super(message);
    }

    public ObjectCreationException(Throwable cause) {
        super(cause);
    }
}
