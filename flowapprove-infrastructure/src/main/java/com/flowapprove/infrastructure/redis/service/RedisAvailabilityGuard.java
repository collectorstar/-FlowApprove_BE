package com.flowapprove.infrastructure.redis.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class RedisAvailabilityGuard {
    private final Clock clock;
    private final Duration retryAfter;
    private final AtomicReference<Instant> blockedUntil = new AtomicReference<>();

    public RedisAvailabilityGuard(Duration retryAfter) {
        this(Clock.systemUTC(), retryAfter);
    }

    RedisAvailabilityGuard(Clock clock, Duration retryAfter) {
        this.clock = clock;
        this.retryAfter = retryAfter;
    }

    public boolean allowRequest() {
        Instant until = blockedUntil.get();
        return until == null || !clock.instant().isBefore(until);
    }

    public void recordFailure() {
        blockedUntil.set(clock.instant().plus(retryAfter));
    }

    public void recordSuccess() {
        blockedUntil.set(null);
    }
}
