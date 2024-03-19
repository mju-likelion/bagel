package org.mjulikelion.bagel.util.redis;

import static org.mjulikelion.bagel.errorcode.ErrorCode.REDIS_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mjulikelion.bagel.exception.RedisException;
import org.springframework.data.redis.core.StringRedisTemplate;

public class StringRedisUtil<T> {
    private final StringRedisTemplate redisTemplate;
    private final Class<T> valueType;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StringRedisUtil(StringRedisTemplate redisTemplate, Class<T> valueType) {
        this.redisTemplate = redisTemplate;
        this.valueType = valueType;
    }

    public void insert(String key, T value) {
        try {
            String valueString = objectMapper.writeValueAsString(value);
            this.redisTemplate.opsForValue().set(key, valueString);
        } catch (Exception e) {
            throw new RedisException(REDIS_ERROR, e.getMessage());
        }
    }

    public T select(String key) {
        try {
            String valueString = this.redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(valueString, this.valueType);
        } catch (Exception e) {
            throw new RedisException(REDIS_ERROR, e.getMessage());
        }
    }
}
