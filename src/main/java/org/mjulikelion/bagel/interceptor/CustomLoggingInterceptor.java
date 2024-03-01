package org.mjulikelion.bagel.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class CustomLoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ipAddress = getClientIP(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("----- {} {} {} -----", ipAddress, method, uri);

        // 추가적인 로깅
        logUserAgent(request);
        logQueryString(request);
        logRequestHeaders(request);

        log.info("----- Request end -----");

        return true;
    }

    private String getClientIP(HttpServletRequest request) throws UnknownHostException {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.info(String.valueOf(Inet4Address.getByName(ip)));

        return ip;
    }

    private void logUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        log.info("User-Agent: {}", userAgent);
    }

    private void logQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        log.info("Query String: {}", queryString);
    }

    private void logRequestHeaders(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        String userAgent = request.getHeader("User-Agent");
        log.info("Content-Type: {}", contentType);
        log.info("User-Agent: {}", userAgent);
    }
}

