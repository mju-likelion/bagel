package org.mjulikelion.bagel.dto.response.apply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class ApplyExistResponseData {
    @JsonProperty("isExist")
    private Boolean isExist;
}
