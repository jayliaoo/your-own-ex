package org.example.yourownex.controller;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final int code;

    public CustomException(
            int code,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.code = code;
    }

    public CustomException(String message) {
        this(400, message, null);
    }

    public CustomException(Throwable cause) {
        this(400, cause.getMessage(), cause);
    }
}
