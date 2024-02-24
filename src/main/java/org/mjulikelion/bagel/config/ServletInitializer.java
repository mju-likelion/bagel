package org.mjulikelion.bagel.config;

import org.mjulikelion.bagel.filter.ContentCachingRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletInitializer {

    @Bean
    public FilterRegistrationBean<ContentCachingRequestFilter> loggingFilter() {
        FilterRegistrationBean<ContentCachingRequestFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(new ContentCachingRequestFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
