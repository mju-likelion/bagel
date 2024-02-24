package org.mjulikelion.bagel.aspect;

import static org.mjulikelion.bagel.errorcode.ErrorCode.RATE_LIMIT_ERROR;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mjulikelion.bagel.exception.RateLimitException;
import org.mjulikelion.bagel.util.annotaion.ratelimit.RateLimit;
import org.mjulikelion.bagel.util.slack.SlackService;
import org.mjulikelion.bagel.util.slack.asset.message.SlackRateLimitMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    private final SlackService slackService;
    private final Map<String, Bucket> ipBuckets;

    @Autowired
    public RateLimitAspect(SlackService slackService) {
        this.slackService = slackService;
        this.ipBuckets = new ConcurrentHashMap<>();
    }

    @Around("@annotation(rateLimit)")
    public Object rateLimitAdvice(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        HttpServletRequest request = getRequest(joinPoint);
        String ipAddress = getClientIP(request);

        Bucket bucket = ipBuckets.computeIfAbsent(methodName,
                k -> new LocalBucketBuilder()
                        .addLimit(Bandwidth.classic(rateLimit.limit(),
                                Refill.greedy(rateLimit.limit(), Duration.ofSeconds(rateLimit.duration()))))
                        .build());

        if (!bucket.tryConsume(1)) {
            String requestBody = getRequestBodyAsString(joinPoint);
            this.sendSlackMessage(ipAddress, request.getRequestURL().toString(), requestBody);
            this.logRateLimitExceeded(ipAddress, request.getRequestURL().toString(), requestBody);
            throw new RateLimitException(RATE_LIMIT_ERROR);
        }

        return joinPoint.proceed();
    }

    private void sendSlackMessage(String ipAddress, String requestUrl, String requestBody) {
        this.slackService.sendSlackMessage(
                new SlackRateLimitMessage(ipAddress, requestUrl, requestBody));
    }

    private HttpServletRequest getRequest(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }

    private String getRequestBodyAsString(ProceedingJoinPoint joinPoint) throws UnsupportedEncodingException {
        HttpServletRequest request = getRequest(joinPoint);
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] requestBody = wrapper.getContentAsByteArray();
            String requestBodyString = new String(requestBody, request.getCharacterEncoding());
            return requestBodyString.isBlank() ? "No request body" : requestBodyString;
        }
        return null;
    }

    private void logRateLimitExceeded(String ipAddress, String requestUrl, String requestBody) {
        log.warn("Rate limit exceeded for IP: {} and URL: {}, request body: {}", ipAddress, requestUrl, requestBody);
    }

    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
