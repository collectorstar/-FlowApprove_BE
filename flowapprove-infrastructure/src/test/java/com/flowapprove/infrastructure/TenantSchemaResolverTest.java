package com.flowapprove.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flowapprove.infrastructure.tenant.TenantSchemaResolver;
import org.junit.jupiter.api.Test;

class TenantSchemaResolverTest {

    @Test
    void normalizesTenantCodeIntoSchemaName() {
        TenantSchemaResolver resolver = new TenantSchemaResolver();

        assertEquals("tenant_finance_team", resolver.resolveSchemaName("Finance-Team"));
    }
}
