package org.mjulikelion.bagel.constant;

public class RegexPatterns {
    public static final String APPLICATION_USER_ID_PATTERN = "^[a-zA-Z][0-9a-zA-Z]{6,12}$";//영문자로 시작하고, 그 뒤로 숫자와 영문자의 조합이 6에서 12개까지인 문자열
    public static final String APPLICATION_NAME_PATTERN = "^[가-힣]{1,6}$";//1에서 6글자의 한글로 이루어진 문자열
    public static final String APPLICATION_STUDENT_ID_PATTERN = "^60\\d{6}$";//60으로 시작하고 그 뒤로 숫자 6개인 문자열
    public static final String APPLICATION_PHONE_NUMBER_PATTERN = "^010-\\d{4}-\\d{4}$";//010-숫자4개-숫자4개
}
