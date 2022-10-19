package com.lukeboxwalker.command;

public class CommandLoadingException extends RuntimeException {

    static final long serialVersionUID = 42L;

    public CommandLoadingException(String message) {
        super(message);
    }

    public CommandLoadingException(Throwable cause) {
        super(cause);
    }
}
