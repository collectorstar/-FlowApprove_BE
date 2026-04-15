package com.flowapprove.infrastructure.tenant;

import java.util.Optional;

public final class TenantContextHolder {
    private static final ThreadLocal<TenantContext> HOLDER = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void set(TenantContext tenantContext) {
        HOLDER.set(tenantContext);
    }

    public static Optional<TenantContext> get() {
        return Optional.ofNullable(HOLDER.get());
    }

    public static void clear() {
        HOLDER.remove();
    }
}
