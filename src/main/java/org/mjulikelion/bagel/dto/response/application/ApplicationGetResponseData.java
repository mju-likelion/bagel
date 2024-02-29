package org.mjulikelion.bagel.dto.response.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.mjulikelion.bagel.model.Agreement;
import org.mjulikelion.bagel.model.Introduce;
import org.mjulikelion.bagel.model.Major;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationGetResponseData {
    @JsonProperty("introduces")
    private List<Introduce> introduces;
    @JsonProperty("agreements")
    private List<Agreement> agreements;
    @JsonProperty("majors")
    private List<Major> majors;
}
