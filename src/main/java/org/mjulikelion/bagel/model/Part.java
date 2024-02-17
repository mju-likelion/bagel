package org.mjulikelion.bagel.model;

import static org.mjulikelion.bagel.errorcode.ErrorCode.INVALID_PART_ERROR;

import org.mjulikelion.bagel.exception.InvalidDataException;

public enum Part {
    SERVER, WEB;

    public static Part findBy(String part) {
        for (Part p : Part.values()) {
            if (p.name().equals(part)) {
                return p;
            }
        }
        throw new InvalidDataException(INVALID_PART_ERROR);
    }
}
