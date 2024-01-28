package org.mjulikelion.bagel.controller;

import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.mjulikelion.bagel.model.Part;
import org.mjulikelion.bagel.service.application.ApplicationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("application")
public class ApplicationController {
    private final ApplicationQueryService applicationQueryService;

    @Autowired
    public ApplicationController(ApplicationQueryService applicationQueryService) {
        this.applicationQueryService = applicationQueryService;
    }

    @GetMapping("/{part}")
    public ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(
            @PathVariable("part") Part part) {
        return this.applicationQueryService.getApplicationByPart(part);
    }
}
