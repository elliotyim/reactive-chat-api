package com.elldev.reactivechat.exception;

import com.elldev.reactivechat.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final Integer BASE_STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private final Integer BASE_ERROR_CODE = ErrorCode.UNDEFINED_ERROR;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleExceptions(Exception e) {
        int status = BASE_STATUS;

        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Error occurred!");
        payload.put("errorCode", BASE_ERROR_CODE);

        if (e instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST.value();
            payload.put("message", e.getMessage());
            payload.put("errorCode", ((BadRequestException) e).errorCode);
        }

        log.error(e.getMessage(), e);

        return ResponseEntity.status(status).body(payload);
    }
}
