package org.mjulikelion.bagel.config;

import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.interceptor.CustomLoggingInterceptor;
import org.mjulikelion.bagel.interceptor.DateRangeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class RequestInterceptorConfig implements WebMvcConfigurer {
    private final DateRangeInterceptor dateRangeInterceptor;
    private final CustomLoggingInterceptor customLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dateRangeInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(customLoggingInterceptor)
                .addPathPatterns("/**");
    }
}
