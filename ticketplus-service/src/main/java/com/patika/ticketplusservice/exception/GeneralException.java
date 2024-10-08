package com.patika.ticketplusservice.exception;

import com.patika.ticketplusservice.model.enums.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GeneralException extends RuntimeException {

    private final String message;

    private final ErrorCode errorCode;

    private final HttpStatus status;

    public GeneralException(String message, HttpStatus status, ErrorCode errorCode) {
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

}
