package com.flowapprove.domain.organization;

import com.flowapprove.domain.common.AggregateRoot;
import com.flowapprove.shared.identity.TenantId;

public class Tenant extends AggregateRoot<TenantId> {
    private final String tenantCode;
    private final String schemaName;

    public Tenant(TenantId id, String tenantCode, String schemaName) {
        super(id);
        this.tenantCode = tenantCode;
        this.schemaName = schemaName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public String getSchemaName() {
        return schemaName;
    }
}
