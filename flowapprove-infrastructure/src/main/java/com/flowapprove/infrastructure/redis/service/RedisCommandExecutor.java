package com.flowapprove.infrastructure.redis.service;

import java.util.function.Supplier;

public interface RedisCommandExecutor {
    <T> T execute(String operationName, Supplier<T> supplier, T fallbackValue);

    void execute(String operationName, Runnable runnable);
}
