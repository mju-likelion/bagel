package org.mjulikelion.bagel.util.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RedisUtilFactory {
    private final StringRedisTemplate stringRedisTemplate;

    public <T> StringRedisUtil<T> createStringRedisUtil(Class<T> valueType) {
        return new StringRedisUtil<>(stringRedisTemplate, valueType);
    }
}
