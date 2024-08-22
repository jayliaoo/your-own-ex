package org.example.yourownex.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.yourownex.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleCustomException(CustomException ex) {
        log.warn(ex.getMessage(), ex);
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.fail(500, ex.getMessage());
    }
}
