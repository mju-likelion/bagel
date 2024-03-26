package org.mjulikelion.bagel.exception;

import org.mjulikelion.bagel.errorcode.ErrorCode;

public class RedisException extends CustomException {
    public RedisException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
