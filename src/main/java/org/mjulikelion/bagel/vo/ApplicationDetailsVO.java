package org.mjulikelion.bagel.vo;

import java.util.List;
import org.mjulikelion.bagel.model.Application;
import org.mjulikelion.bagel.model.ApplicationAgreement;
import org.mjulikelion.bagel.model.ApplicationIntroduce;

public record ApplicationDetailsVO(Application application, List<ApplicationAgreement> agreements,
                                   List<ApplicationIntroduce> introduces) {
}
