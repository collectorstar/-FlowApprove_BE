package com.flowapprove.infrastructure.persistence.memory;

import com.flowapprove.domain.workflow.WorkflowDefinition;
import com.flowapprove.domain.workflow.WorkflowDefinitionRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWorkflowDefinitionRepository implements WorkflowDefinitionRepository {
    private final Map<UUID, WorkflowDefinition> store = new ConcurrentHashMap<>();

    @Override
    public void save(WorkflowDefinition workflowDefinition) {
        store.put(workflowDefinition.getId(), workflowDefinition);
    }

    @Override
    public Optional<WorkflowDefinition> findById(UUID workflowDefinitionId) {
        return Optional.ofNullable(store.get(workflowDefinitionId));
    }
}
