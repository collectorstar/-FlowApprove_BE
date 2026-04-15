package com.flowapprove.dbmigrator.persistence.entity;

import java.time.Instant;
import java.util.UUID;

public record WorkflowRequestEntity(
        UUID id,
        UUID workflowDefinitionId,
        UUID organizationId,
        UUID tenantId,
        UUID requesterId,
        String title,
        String status,
        Instant submittedAt
) {
}
