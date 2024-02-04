package org.mjulikelion.bagel.controller;

import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.apply.ApplyExistResponseData;
import org.mjulikelion.bagel.service.apply.ApplyQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apply")
public class ApplyController {
    private final ApplyQueryService applicationQueryService;

    @Autowired
    public ApplyController(ApplyQueryService applicationQueryService) {
        this.applicationQueryService = applicationQueryService;
    }

    @GetMapping("/exist/{userId}")
    public ResponseEntity<ResponseDto<ApplyExistResponseData>> getApplicationExist(
            @PathVariable("userId") String userId) {
        return this.applicationQueryService.getApplyExist(userId);
    }
}
