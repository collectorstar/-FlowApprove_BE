package com.flowapprove.application.port;

import com.flowapprove.application.workflowrequest.query.WorkflowRequestDetailView;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowRequestReadModelRepository {
    Optional<WorkflowRequestDetailView> findViewById(UUID workflowRequestId);
}
