package org.mjulikelion.bagel.util.redis;

import static org.mjulikelion.bagel.errorcode.ErrorCode.REDIS_ERROR;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.mjulikelion.bagel.exception.RedisException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUtil<K, V> {
    private final RedisTemplate<K, V> redisTemplate;

    public void insert(K key, V value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new RedisException(REDIS_ERROR, e.getMessage());
        }
    }

    public Optional<V> select(K key) {
        try {
            V value = this.redisTemplate.opsForValue().get(key);
            return Optional.ofNullable(value);
        } catch (Exception e) {
            throw new RedisException(REDIS_ERROR, e.getMessage());
        }
    }
}
