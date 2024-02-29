package org.mjulikelion.bagel.util.redis;

import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.dto.response.application.ApplicationGetResponseData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationGetResponseDataRedisUtil {
    private final RedisTemplate<String, ApplicationGetResponseData> applicationGetResponseDataRedisTemplate;

    public void setApplicationGetResponseData(String part, ApplicationGetResponseData applicationGetResponseData) {
        this.applicationGetResponseDataRedisTemplate.opsForValue().set(part, applicationGetResponseData);
    }

    public ApplicationGetResponseData getApplicationGetResponseData(String part) {
        return this.applicationGetResponseDataRedisTemplate.opsForValue().get(part);
    }
}
