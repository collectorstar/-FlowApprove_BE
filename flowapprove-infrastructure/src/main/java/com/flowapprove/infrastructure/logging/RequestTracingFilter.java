package com.flowapprove.infrastructure.logging;

import com.flowapprove.infrastructure.tenant.TenantContext;
import com.flowapprove.infrastructure.tenant.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

public class RequestTracingFilter extends OncePerRequestFilter {
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String TENANT_REQUEST_ATTRIBUTE = RequestTracingFilter.class.getName() + ".tenant";
    public static final String USER_REQUEST_ATTRIBUTE = RequestTracingFilter.class.getName() + ".user";

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestTracingFilter.class);
    private static final String TRACE_ID_KEY = "traceId";
    private static final String TENANT_KEY = "tenant";
    private static final String USER_KEY = "user";
    private static final String METHOD_KEY = "method";
    private static final String PATH_KEY = "path";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String traceId = resolveTraceId(request);
        long startedAt = System.nanoTime();

        MDC.put(TRACE_ID_KEY, traceId);
        MDC.put(METHOD_KEY, request.getMethod());
        MDC.put(PATH_KEY, request.getRequestURI());
        response.setHeader(TRACE_ID_HEADER, traceId);

        try {
            LOGGER.info("Incoming request");
            filterChain.doFilter(request, response);
        } finally {
            putTenantContext(request);
            putUserContext(request);
            long durationMs = (System.nanoTime() - startedAt) / 1_000_000;
            LOGGER.info("Request completed with status={} durationMs={}", response.getStatus(), durationMs);
            clearContext();
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String incomingTraceId = request.getHeader(TRACE_ID_HEADER);
        if (incomingTraceId != null && !incomingTraceId.isBlank()) {
            return incomingTraceId.trim();
        }
        return UUID.randomUUID().toString();
    }

    private void putTenantContext(HttpServletRequest request) {
        Object tenantFromRequest = request.getAttribute(TENANT_REQUEST_ATTRIBUTE);
        if (tenantFromRequest instanceof String tenantCode && !tenantCode.isBlank()) {
            MDC.put(TENANT_KEY, tenantCode);
            return;
        }
        TenantContextHolder.get()
                .map(TenantContext::tenantCode)
                .filter(tenantCode -> !tenantCode.isBlank())
                .ifPresent(tenantCode -> MDC.put(TENANT_KEY, tenantCode));
    }

    private void putUserContext(HttpServletRequest request) {
        Object userFromRequest = request.getAttribute(USER_REQUEST_ATTRIBUTE);
        if (userFromRequest instanceof String userId && !userId.isBlank()) {
            MDC.put(USER_KEY, userId);
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            if (userId != null && !userId.isBlank()) {
                MDC.put(USER_KEY, userId);
            }
            return;
        }
        String name = authentication.getName();
        if (name != null && !name.isBlank()) {
            MDC.put(USER_KEY, name);
        }
    }

    private void clearContext() {
        MDC.remove(TRACE_ID_KEY);
        MDC.remove(TENANT_KEY);
        MDC.remove(USER_KEY);
        MDC.remove(METHOD_KEY);
        MDC.remove(PATH_KEY);
    }
}
