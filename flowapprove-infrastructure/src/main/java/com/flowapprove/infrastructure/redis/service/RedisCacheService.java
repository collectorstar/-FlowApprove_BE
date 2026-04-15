package com.flowapprove.infrastructure.redis.service;

import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisCacheService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisCommandExecutor redisCommandExecutor;
    private final Duration defaultTtl;

    public RedisCacheService(
            StringRedisTemplate stringRedisTemplate,
            RedisCommandExecutor redisCommandExecutor,
            Duration defaultTtl
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisCommandExecutor = redisCommandExecutor;
        this.defaultTtl = defaultTtl;
    }

    public Optional<String> get(String key) {
        return redisCommandExecutor.execute(
                "get:" + key,
                () -> Optional.ofNullable(stringRedisTemplate.opsForValue().get(key)),
                Optional.empty()
        );
    }

    public void put(String key, String value) {
        put(key, value, defaultTtl);
    }

    public void put(String key, String value, Duration ttl) {
        redisCommandExecutor.execute("set:" + key, () -> stringRedisTemplate.opsForValue().set(key, value, ttl));
    }

    public boolean delete(String key) {
        return redisCommandExecutor.execute(
                "delete:" + key,
                () -> Boolean.TRUE.equals(stringRedisTemplate.delete(key)),
                false
        );
    }

    public boolean tryAcquireLock(String key, String value, Duration ttl) {
        return redisCommandExecutor.execute(
                "lock:" + key,
                () -> Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, ttl)),
                false
        );
    }
}
