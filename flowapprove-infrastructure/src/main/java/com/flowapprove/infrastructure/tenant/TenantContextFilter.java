package com.flowapprove.infrastructure.tenant;

import com.flowapprove.infrastructure.logging.RequestTracingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

public class TenantContextFilter extends OncePerRequestFilter {
    private static final String DEV_HEADER = "X-Tenant-Code";

    private final TenantSchemaResolver tenantSchemaResolver;
    private final boolean allowHeaderOverride;

    public TenantContextFilter(TenantSchemaResolver tenantSchemaResolver, boolean allowHeaderOverride) {
        this.tenantSchemaResolver = tenantSchemaResolver;
        this.allowHeaderOverride = allowHeaderOverride;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String tenantCode = resolveTenantCode(request);
            if (tenantCode != null && !tenantCode.isBlank()) {
                request.setAttribute(RequestTracingFilter.TENANT_REQUEST_ATTRIBUTE, tenantCode);
                TenantContextHolder.set(new TenantContext(tenantCode, tenantSchemaResolver.resolveSchemaName(tenantCode)));
            }
            resolveUserId().ifPresent(userId -> request.setAttribute(RequestTracingFilter.USER_REQUEST_ATTRIBUTE, userId));
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }

    private String resolveTenantCode(HttpServletRequest request) {
        if (allowHeaderOverride) {
            String headerTenant = request.getHeader(DEV_HEADER);
            if (headerTenant != null && !headerTenant.isBlank()) {
                return headerTenant;
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("tenant");
        }
        return null;
    }

    private java.util.Optional<String> resolveUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return java.util.Optional.empty();
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            if (userId != null && !userId.isBlank()) {
                return java.util.Optional.of(userId);
            }
        }
        String name = authentication.getName();
        if (name == null || name.isBlank()) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(name);
    }
}
