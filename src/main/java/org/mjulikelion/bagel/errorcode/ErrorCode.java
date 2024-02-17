package org.mjulikelion.bagel.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //서버 내부 오류들
    INTERNAL_SERVER_ERROR("INTERNAL 5000", "알 수 없는 서버 내부 오류."),//서버 내부 오류
    FILE_STORAGE_ERROR("INTERNAL 5001", "파일 저장에 실패하였습니다."),//파일 저장 실패
    //잘못된 경로, 메소드 오류들
    METHOD_NOT_ALLOWED_ERROR("METHOD 4050", "허용되지 않은 메소드 입니다."),//허용되지 않은 메소드
    NO_RESOURCE_ERROR("RESOURCE 4040", "해당 리소스를 찾을 수 없습니다."),//리소스를 찾을 수 없음(잘못된 URI)
    //지원서 관련 오류들
    APPLICATION_ALREADY_EXISTS_ERROR("APPLICATION 4090", "이미 지원서가 존재합니다."),//이미 지원서가 존재함
    //잘못된 데이터 오류들
    INVALID_REQUEST_FORMAT_ERROR("INVALID 4000", "유효하지 않은 Body 형식입니다."),//요청 형식이 잘못됨
    INVALID_MAJOR_ERROR("INVALID 4001", "유효하지 않은 학과입니다."),//유효하지 않은 학과
    INVALID_PART_ERROR("INVALID 4002", "유효하지 않은 파트입니다."),//유효하지 않은 파트
    INVALID_INTRODUCE_ERROR("INVALID 4003", "유효하지 않은 자기소개 항목 입니다."),//유효하지 않은 자기소개 항목
    INVALID_AGREEMENT_ERROR("INVALID 4004", "유효하지 않은 동의 항목 입니다."),//유효하지 않은 동의 항목
    INVALID_INTRODUCE_MISSING_ERROR("INVALID 4005", "자기소개 항목이 누락되었습니다."),//자기소개 항목이 누락됨
    INVALID_AGREEMENT_MISSING_ERROR("INVALID 4006", "동의 항목이 누락되었습니다.");//동의 항목이 누락됨


    private final String code;
    private final String message;
}
