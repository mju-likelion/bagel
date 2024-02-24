package org.mjulikelion.bagel.controller;

import static org.mjulikelion.bagel.constant.RegexPatterns.APPLICATION_STUDENT_ID_PATTERN;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.request.ApplySaveDto;
import org.mjulikelion.bagel.dto.response.ResponseDto;
import org.mjulikelion.bagel.dto.response.apply.ApplyExistResponseData;
import org.mjulikelion.bagel.service.apply.ApplyCommandService;
import org.mjulikelion.bagel.service.apply.ApplyQueryService;
import org.mjulikelion.bagel.util.annotaion.ratelimit.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("apply")
public class ApplyController {
    private final ApplyQueryService applyQueryService;
    private final ApplyCommandService applyCommandService;

    @RateLimit
    @GetMapping("/exist/{studentId}")
    public ResponseEntity<ResponseDto<ApplyExistResponseData>> getApplicationExist(
            @PathVariable("studentId") @Pattern(regexp = APPLICATION_STUDENT_ID_PATTERN) @Valid String studentId) {
        return this.applyQueryService.getApplyExist(studentId);
    }

    @RateLimit
    @PostMapping()
    public ResponseEntity<ResponseDto<Void>> saveApply(
            @RequestBody @Valid ApplySaveDto applySaveDto) {
        return this.applyCommandService.saveApply(applySaveDto);
    }
}
