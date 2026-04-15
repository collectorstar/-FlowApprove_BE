package com.flowapprove.infrastructure.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flowapprove.infrastructure.redis.config.RedisProperties;
import com.flowapprove.infrastructure.redis.key.RedisKeyRegistry;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RedisKeyRegistryTest {

    @Test
    void buildsNamespacedKeys() {
        RedisProperties properties = new RedisProperties();
        properties.setKeyPrefix("fa");
        RedisKeyRegistry registry = new RedisKeyRegistry(properties);

        assertEquals("fa:tenant:finance:cache:users", registry.tenantCache("finance", "users"));
        assertEquals("fa:workflow-request:123e4567-e89b-12d3-a456-426614174000:detail",
                registry.workflowRequestDetail(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
    }
}
