package com.flowapprove.shared.security;

import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;
import com.flowapprove.shared.identity.UserId;
import java.util.Set;

public record CurrentPrincipal(
        UserId userId,
        TenantId tenantId,
        OrganizationId organizationId,
        Set<Role> roles
) {
}
