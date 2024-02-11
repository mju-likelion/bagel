package org.mjulikelion.bagel.controller;

import jakarta.validation.Valid;
import org.mjulikelion.bagel.dto.request.ApplicationSaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.mjulikelion.bagel.service.application.ApplicationCommandService;
import org.mjulikelion.bagel.service.application.ApplicationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("application")
public class ApplicationController {
    private final ApplicationQueryService applicationQueryService;
    private final ApplicationCommandService applicationCommandService;

    @Autowired
    public ApplicationController(ApplicationQueryService applicationQueryService,
                                 ApplicationCommandService applicationCommandService) {
        this.applicationQueryService = applicationQueryService;
        this.applicationCommandService = applicationCommandService;
    }

    @GetMapping("/{part}")
    public ResponseEntity<ResponseDto<ApplicationGetResponseData>> getApplicationByPart(
            @PathVariable("part") String part) {
        return this.applicationQueryService.getApplicationByPart(part);
    }

    @PostMapping()
    public ResponseEntity<ResponseDto<Void>> saveApplication(
            @RequestBody @Valid ApplicationSaveDto applicationSaveDto) {
        return this.applicationCommandService.saveApplication(applicationSaveDto);
    }
}
