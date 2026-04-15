package com.flowapprove.domain.request;

import com.flowapprove.domain.common.AggregateRoot;
import com.flowapprove.shared.identity.UserId;
import java.util.UUID;

public class ApprovalTask extends AggregateRoot<UUID> {
    private final UUID workflowRequestId;
    private final String stepName;
    private final UserId assigneeId;
    private final ApprovalTaskStatus status;

    public ApprovalTask(UUID id, UUID workflowRequestId, String stepName, UserId assigneeId, ApprovalTaskStatus status) {
        super(id);
        this.workflowRequestId = workflowRequestId;
        this.stepName = stepName;
        this.assigneeId = assigneeId;
        this.status = status;
    }

    public UUID getWorkflowRequestId() {
        return workflowRequestId;
    }

    public String getStepName() {
        return stepName;
    }

    public UserId getAssigneeId() {
        return assigneeId;
    }

    public ApprovalTaskStatus getStatus() {
        return status;
    }
}
