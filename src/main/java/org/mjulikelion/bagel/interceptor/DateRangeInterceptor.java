package org.mjulikelion.bagel.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.bagel.config.DateRangeConfig;
import org.mjulikelion.bagel.exception.DateRangeException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
@Slf4j
public class DateRangeInterceptor implements HandlerInterceptor {
    private final DateRangeConfig dateRangeConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LocalDate startDate = dateRangeConfig.getStartDate();
        LocalDate endDate = dateRangeConfig.getEndDate();
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
            throw new DateRangeException("Request Date is out of range." +
                    " Start Date: " + startDate + ", End Date: " + endDate + ", Requested Date: " + currentDate);
        }
        return true;
    }
}
