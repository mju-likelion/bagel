package org.mjulikelion.bagel.dto.response.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import org.mjulikelion.bagel.model.Agreement;
import org.mjulikelion.bagel.model.Introduce;

@Builder
public class ApplicationGetResponseData {
    @JsonProperty("introduces")
    private List<Introduce> introduces;
    @JsonProperty("agreements")
    private List<Agreement> agreements;
}
