package com.flowapprove.domain.organization;

import com.flowapprove.domain.common.AggregateRoot;
import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;

public class Organization extends AggregateRoot<OrganizationId> {
    private final TenantId tenantId;
    private final String name;

    public Organization(OrganizationId id, TenantId tenantId, String name) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }
}
