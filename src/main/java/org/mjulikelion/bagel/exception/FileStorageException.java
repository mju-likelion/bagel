package org.mjulikelion.bagel.exception;

import lombok.Getter;
import org.mjulikelion.bagel.errorcode.ErrorCode;

@Getter
public class FileStorageException extends CustomException {
    public FileStorageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
