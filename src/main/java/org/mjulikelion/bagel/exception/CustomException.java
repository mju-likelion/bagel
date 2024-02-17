package org.mjulikelion.bagel.exception;

import lombok.Getter;
import org.mjulikelion.bagel.errorcode.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    protected ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
