package com.flowapprove.shared.identity;

import java.util.UUID;

public record TenantId(UUID value) {
    public static TenantId newId() {
        return new TenantId(UUID.randomUUID());
    }
}
