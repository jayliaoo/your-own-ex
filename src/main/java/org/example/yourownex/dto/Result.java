package org.example.yourownex.dto;

public record Result<T>(int code, String message, T data) {
    public static <T> Result<T> success(T data) {
        return new Result<>(200, null, data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, null, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
