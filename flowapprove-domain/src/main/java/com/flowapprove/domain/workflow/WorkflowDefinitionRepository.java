package com.flowapprove.domain.workflow;

import java.util.Optional;
import java.util.UUID;

public interface WorkflowDefinitionRepository {
    void save(WorkflowDefinition workflowDefinition);

    Optional<WorkflowDefinition> findById(UUID workflowDefinitionId);
}
