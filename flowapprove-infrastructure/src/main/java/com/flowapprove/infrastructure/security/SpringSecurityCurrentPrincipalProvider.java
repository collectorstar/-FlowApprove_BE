package com.flowapprove.infrastructure.security;

import com.flowapprove.application.port.CurrentPrincipalProvider;
import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;
import com.flowapprove.shared.identity.UserId;
import com.flowapprove.shared.security.CurrentPrincipal;
import com.flowapprove.shared.security.Role;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SpringSecurityCurrentPrincipalProvider implements CurrentPrincipalProvider {
    @Override
    public CurrentPrincipal getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Set<Role> roles = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            return new CurrentPrincipal(
                    new UserId(UUID.fromString(jwt.getSubject())),
                    new TenantId(UUID.fromString(jwt.getClaimAsString("tenant_id"))),
                    new OrganizationId(UUID.fromString(jwt.getClaimAsString("org"))),
                    roles
            );
        }

        return new CurrentPrincipal(
                UserId.newId(),
                TenantId.newId(),
                OrganizationId.newId(),
                Set.of(Role.ORG_OWNER, Role.ORG_ADMIN, Role.REQUESTER, Role.APPROVER)
        );
    }
}
