package org.mjulikelion.bagel.exception;

import org.mjulikelion.bagel.errorcode.ErrorCode;

public class RateLimitException extends CustomException {
    public RateLimitException(ErrorCode errorCode) {
        super(errorCode);
    }
}
