package org.mjulikelion.bagel.dto.response.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class ApplicationExistResponseData {
    @JsonProperty("isExist")
    private Boolean isExist;
}
