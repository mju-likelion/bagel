package org.mjulikelion.bagel.exception;

import org.mjulikelion.bagel.errorcode.ErrorCode;

public class JpaException extends CustomException {
    public JpaException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
