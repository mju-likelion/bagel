package org.mjulikelion.bagel.constant;

public class RegexPatterns {
    public static final String APPLICATION_NAME_PATTERN = "^[가-힣]{1,6}$";//1에서 6글자의 한글로 이루어진 문자열
    public static final String APPLICATION_STUDENT_ID_PATTERN = "^60\\d{6}$";//60으로 시작하고 그 뒤로 숫자 6개인 문자열
    public static final String APPLICATION_PHONE_NUMBER_PATTERN = "^010-\\d{4}-\\d{4}$";//010-숫자4개-숫자4개
    public static final String APPLICATION_GRADE_PATTERN = "^[1-4]$|휴학|졸업유예";//1에서 4까지의 숫자 또는 휴학 또는 졸업유예
}
