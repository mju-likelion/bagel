package org.mjulikelion.bagel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class ResponseDto<T> {
    private final int statusCode;
    private final String message;
    private final T data;

    public ResponseDto(final HttpStatus statusCode, final String resultMsg) {
        this.statusCode = statusCode.value();
        this.message = resultMsg;
        this.data = null;
    }

    public static <T> ResponseDto<T> res(final HttpStatus statusCode, final String resultMsg) {
        return res(statusCode, resultMsg, null);
    }

    public static <T> ResponseDto<T> res(final HttpStatus statusCode, final String resultMsg, final T t) {
        return ResponseDto.<T>builder()
                .data(t)
                .statusCode(statusCode.value())
                .message(resultMsg)
                .build();
    }
}
