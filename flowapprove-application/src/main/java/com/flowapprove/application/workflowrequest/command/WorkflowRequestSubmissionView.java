package com.flowapprove.application.workflowrequest.command;

import com.flowapprove.domain.request.WorkflowRequestStatus;
import java.time.Instant;
import java.util.UUID;

public record WorkflowRequestSubmissionView(
        UUID workflowRequestId,
        UUID workflowDefinitionId,
        String title,
        WorkflowRequestStatus status,
        Instant submittedAt
) {
}
