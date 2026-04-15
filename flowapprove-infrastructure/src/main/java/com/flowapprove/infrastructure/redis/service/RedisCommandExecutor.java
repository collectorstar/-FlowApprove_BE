package com.flowapprove.infrastructure.redis.service;

import java.time.Duration;
import java.util.function.Supplier;

public interface RedisCommandExecutor {
    default <T> T execute(String operationName, Supplier<T> supplier, T fallbackValue) {
        return execute(operationName, supplier, fallbackValue, null);
    }

    <T> T execute(String operationName, Supplier<T> supplier, T fallbackValue, Duration timeout);

    default void execute(String operationName, Runnable runnable) {
        execute(operationName, runnable, null);
    }

    void execute(String operationName, Runnable runnable, Duration timeout);
}
