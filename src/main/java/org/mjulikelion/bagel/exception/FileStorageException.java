package org.mjulikelion.bagel.exception;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
}
