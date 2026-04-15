package com.flowapprove.infrastructure.redis.service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

public class FailOpenRedisCommandExecutor implements RedisCommandExecutor, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(FailOpenRedisCommandExecutor.class);

    private final boolean enabled;
    private final RedisAvailabilityGuard availabilityGuard;
    private final Duration defaultTimeout;
    private final ExecutorService commandExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public FailOpenRedisCommandExecutor(boolean enabled, RedisAvailabilityGuard availabilityGuard, Duration defaultTimeout) {
        this.enabled = enabled;
        this.availabilityGuard = availabilityGuard;
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public <T> T execute(String operationName, Supplier<T> supplier, T fallbackValue, Duration timeout) {
        if (!enabled || !availabilityGuard.allowRequest()) {
            return fallbackValue;
        }

        try {
            T value = executeWithTimeout(supplier, resolveTimeout(timeout));
            availabilityGuard.recordSuccess();
            return value;
        } catch (RuntimeException ex) {
            availabilityGuard.recordFailure();
            LOGGER.warn("Redis operation '{}' failed. Falling back without interrupting request flow.", operationName, ex);
            return fallbackValue;
        }
    }

    @Override
    public void execute(String operationName, Runnable runnable, Duration timeout) {
        execute(operationName, () -> {
            runnable.run();
            return Boolean.TRUE;
        }, Boolean.FALSE, timeout);
    }

    @Override
    public void destroy() {
        commandExecutor.shutdownNow();
    }

    private Duration resolveTimeout(Duration timeout) {
        if (timeout != null && !timeout.isNegative() && !timeout.isZero()) {
            return timeout;
        }
        return defaultTimeout;
    }

    private <T> T executeWithTimeout(Supplier<T> supplier, Duration timeout) {
        Future<T> future = commandExecutor.submit(supplier::get);
        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            future.cancel(true);
            throw new IllegalStateException("Redis operation timed out after " + timeout, ex);
        } catch (InterruptedException ex) {
            future.cancel(true);
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Redis operation interrupted", ex);
        } catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Redis operation failed", cause);
        }
    }
}
