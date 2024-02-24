package org.mjulikelion.bagel.controller;

import org.mjulikelion.bagel.util.annotaion.ratelimit.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @RateLimit
    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
