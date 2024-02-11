package org.mjulikelion.bagel.model;

public enum Part {
    SERVER, WEB;

    public static Part findBy(String part) {
        for (Part p : Part.values()) {
            if (p.name().equals(part)) {
                return p;
            }
        }
        return null;
    }
}
