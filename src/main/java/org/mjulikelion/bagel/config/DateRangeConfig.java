package org.mjulikelion.bagel.config;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DateRangeConfig {
    @Value("${apply.start.date}")
    private String startDateString;

    @Value("${apply.end.date}")
    private String endDateString;

    public LocalDate getStartDate() {
        return LocalDate.parse(startDateString);
    }

    public LocalDate getEndDate() {
        return LocalDate.parse(endDateString);
    }
}
