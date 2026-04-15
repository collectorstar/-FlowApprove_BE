package com.flowapprove.shared.identity;

import java.util.UUID;

public record OrganizationId(UUID value) {
    public static OrganizationId newId() {
        return new OrganizationId(UUID.randomUUID());
    }
}
