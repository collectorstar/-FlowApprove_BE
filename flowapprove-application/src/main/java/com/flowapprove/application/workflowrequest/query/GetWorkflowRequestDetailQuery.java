package com.flowapprove.application.workflowrequest.query;

import com.flowapprove.application.cqrs.Query;
import java.util.UUID;

public record GetWorkflowRequestDetailQuery(UUID workflowRequestId) implements Query<WorkflowRequestDetailView> {
}
