package com.flowapprove.infrastructure.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.flowapprove.infrastructure.redis.service.FailOpenRedisCommandExecutor;
import com.flowapprove.infrastructure.redis.service.RedisAvailabilityGuard;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class FailOpenRedisCommandExecutorTest {

    @Test
    void returnsFallbackWhenRedisThrows() {
        FailOpenRedisCommandExecutor executor = new FailOpenRedisCommandExecutor(
                true,
                new RedisAvailabilityGuard(Duration.ofSeconds(30)),
                Duration.ofMillis(500)
        );

        String value = executor.execute("test", () -> {
            throw new IllegalStateException("redis down");
        }, "fallback");

        assertEquals("fallback", value);
    }

    @Test
    void skipsExecutionWhenRedisFeatureDisabled() {
        FailOpenRedisCommandExecutor executor = new FailOpenRedisCommandExecutor(
                false,
                new RedisAvailabilityGuard(Duration.ofSeconds(30)),
                Duration.ofMillis(500)
        );

        boolean result = executor.execute("test", () -> true, false);

        assertFalse(result);
    }

    @Test
    void returnsFallbackWhenRedisTimesOut() {
        FailOpenRedisCommandExecutor executor = new FailOpenRedisCommandExecutor(
                true,
                new RedisAvailabilityGuard(Duration.ofSeconds(30)),
                Duration.ofMillis(500)
        );

        String value = executor.execute("test", () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return "redis-value";
        }, "fallback", Duration.ofMillis(10));

        assertEquals("fallback", value);
    }
}
