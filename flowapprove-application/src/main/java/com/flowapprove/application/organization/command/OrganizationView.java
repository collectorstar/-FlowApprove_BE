package com.flowapprove.application.organization.command;

import java.util.UUID;

public record OrganizationView(UUID organizationId, UUID tenantId, String organizationName, String tenantCode) {
}
