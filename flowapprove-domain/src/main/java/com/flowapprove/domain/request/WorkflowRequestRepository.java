package com.flowapprove.domain.request;

import java.util.Optional;
import java.util.UUID;

public interface WorkflowRequestRepository {
    void save(WorkflowRequest workflowRequest);

    Optional<WorkflowRequest> findById(UUID workflowRequestId);
}
