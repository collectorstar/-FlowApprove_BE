package com.flowapprove.infrastructure.tenant;

public class TenantSchemaResolver {
    public String resolveSchemaName(String tenantCode) {
        return "tenant_" + tenantCode.trim().toLowerCase().replace('-', '_');
    }
}
