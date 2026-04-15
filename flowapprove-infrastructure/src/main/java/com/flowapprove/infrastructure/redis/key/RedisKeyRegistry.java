package com.flowapprove.infrastructure.redis.key;

import com.flowapprove.infrastructure.redis.config.RedisProperties;
import java.util.UUID;

public class RedisKeyRegistry {
    private final String prefix;

    public RedisKeyRegistry(RedisProperties redisProperties) {
        this.prefix = redisProperties.getKeyPrefix();
    }

    public String tenantCache(String tenantCode, String segment) {
        return namespaced("tenant", tenantCode, "cache", segment);
    }

    public String workflowRequestDetail(UUID workflowRequestId) {
        return namespaced("workflow-request", workflowRequestId.toString(), "detail");
    }

    public String pendingApprovals(String tenantCode) {
        return namespaced("tenant", tenantCode, "approvals", "pending");
    }

    public String distributedLock(String resourceName, String resourceId) {
        return namespaced("lock", resourceName, resourceId);
    }

    private String namespaced(String... parts) {
        return prefix + ":" + String.join(":", parts);
    }
}
