package com.flowapprove.application.approval.query;

import com.flowapprove.domain.request.ApprovalTaskStatus;
import java.util.UUID;

public record PendingApprovalView(
        UUID approvalTaskId,
        UUID workflowRequestId,
        String stepName,
        UUID assigneeId,
        ApprovalTaskStatus status
) {
}
