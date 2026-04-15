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
        return get(key, null);
    }

    public Optional<String> get(String key, Duration timeout) {
        return redisCommandExecutor.execute(
                "get:" + key,
                () -> Optional.ofNullable(stringRedisTemplate.opsForValue().get(key)),
                Optional.empty(),
                timeout
        );
    }

    public void put(String key, String value) {
        put(key, value, defaultTtl);
    }

    public void put(String key, String value, Duration ttl) {
        put(key, value, ttl, null);
    }

    public void put(String key, String value, Duration ttl, Duration timeout) {
        redisCommandExecutor.execute("set:" + key, () -> stringRedisTemplate.opsForValue().set(key, value, ttl), timeout);
    }

    public boolean delete(String key) {
        return delete(key, null);
    }

    public boolean delete(String key, Duration timeout) {
        return redisCommandExecutor.execute(
                "delete:" + key,
                () -> Boolean.TRUE.equals(stringRedisTemplate.delete(key)),
                false,
                timeout
        );
    }

    public boolean tryAcquireLock(String key, String value, Duration ttl) {
        return tryAcquireLock(key, value, ttl, null);
    }

    public boolean tryAcquireLock(String key, String value, Duration ttl, Duration timeout) {
        return redisCommandExecutor.execute(
                "lock:" + key,
                () -> Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, ttl)),
                false,
                timeout
        );
    }
}
