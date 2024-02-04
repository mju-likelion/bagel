package org.mjulikelion.bagel.controller;

import jakarta.validation.Valid;
import org.mjulikelion.bagel.dto.request.ApplySaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.apply.ApplyExistResponseData;
import org.mjulikelion.bagel.service.apply.ApplyCommandService;
import org.mjulikelion.bagel.service.apply.ApplyQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apply")
public class ApplyController {
    private final ApplyQueryService applicationQueryService;
    private final ApplyCommandService applicationCommandService;

    @Autowired
    public ApplyController(ApplyQueryService applicationQueryService, ApplyCommandService applicationCommandService) {
        this.applicationQueryService = applicationQueryService;
        this.applicationCommandService = applicationCommandService;
    }

    @GetMapping("/exist/{userId}")
    public ResponseEntity<ResponseDto<ApplyExistResponseData>> getApplicationExist(
            @PathVariable("userId") String userId) {
        return this.applicationQueryService.getApplyExist(userId);
    }

    @PostMapping()
    public ResponseEntity<ResponseDto<Void>> saveApply(
            @RequestBody @Valid ApplySaveDto applySaveDto) {
        return this.applicationCommandService.saveApply(applySaveDto);
    }
}
