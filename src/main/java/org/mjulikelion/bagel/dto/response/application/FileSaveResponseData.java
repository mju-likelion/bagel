package org.mjulikelion.bagel.dto.response.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveResponseData {
    @JsonProperty
    private String url;
}
