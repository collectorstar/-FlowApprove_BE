package com.flowapprove.infrastructure.redis.service;

import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailOpenRedisCommandExecutor implements RedisCommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FailOpenRedisCommandExecutor.class);

    private final boolean enabled;
    private final RedisAvailabilityGuard availabilityGuard;

    public FailOpenRedisCommandExecutor(boolean enabled, RedisAvailabilityGuard availabilityGuard) {
        this.enabled = enabled;
        this.availabilityGuard = availabilityGuard;
    }

    @Override
    public <T> T execute(String operationName, Supplier<T> supplier, T fallbackValue) {
        if (!enabled || !availabilityGuard.allowRequest()) {
            return fallbackValue;
        }

        try {
            T value = supplier.get();
            availabilityGuard.recordSuccess();
            return value;
        } catch (RuntimeException ex) {
            availabilityGuard.recordFailure();
            LOGGER.warn("Redis operation '{}' failed. Falling back without interrupting request flow.", operationName, ex);
            return fallbackValue;
        }
    }

    @Override
    public void execute(String operationName, Runnable runnable) {
        execute(operationName, () -> {
            runnable.run();
            return Boolean.TRUE;
        }, Boolean.FALSE);
    }
}
