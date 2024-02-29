package org.mjulikelion.bagel.exception;

import lombok.Getter;
import org.mjulikelion.bagel.errorcode.ErrorCode;

@Getter
public class InvalidDataException extends CustomException {
    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
    }
}
