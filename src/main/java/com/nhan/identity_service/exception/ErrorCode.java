package com.nhan.identity_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
  SUCCESS(1000, "Success", HttpStatus.OK),
  INVALID_KEY(1001, "Invalid Key", HttpStatus.BAD_REQUEST),
  USER_EXISTED(1002, "User Existed", HttpStatus.BAD_REQUEST),
  UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1004, "Unauthorized", HttpStatus.FORBIDDEN),
  USER_NOT_EXISTED(1005, "User Not Existed", HttpStatus.NOT_FOUND),
  INVALID_INPUT(1006, "Invalid Input", HttpStatus.BAD_REQUEST),
  TOKEN_CANNOT_CREATE(1007, "Token Cannot Create", HttpStatus.BAD_REQUEST),
  INVALID_TOKEN(1008, "Token Invalid ", HttpStatus.UNAUTHORIZED),
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),

  ;

  int code;
  String message;
  HttpStatus httpStatus;

  ErrorCode(int code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
