package com.nhan.identity_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    SUCCESS(1000, "Success", HttpStatus.OK),
    INVALID_KEY(1001,"Invalid Key",HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002,"User Existed",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED()

    int code;
    String message;
    HttpStatus httpStatus;
    ErrorCode(int code , String message , HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
