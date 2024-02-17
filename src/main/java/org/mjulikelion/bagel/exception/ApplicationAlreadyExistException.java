package org.mjulikelion.bagel.exception;

import org.mjulikelion.bagel.errorcode.ErrorCode;

public class ApplicationAlreadyExistException extends CustomException {
    public ApplicationAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
