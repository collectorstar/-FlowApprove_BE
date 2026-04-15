package com.flowapprove.application.workflowrequest.query;

import com.flowapprove.domain.request.WorkflowRequestStatus;
import java.time.Instant;
import java.util.UUID;

public record WorkflowRequestDetailView(
        UUID workflowRequestId,
        UUID workflowDefinitionId,
        UUID organizationId,
        UUID tenantId,
        UUID requesterId,
        String title,
        WorkflowRequestStatus status,
        Instant submittedAt
) {
}
