package com.flowapprove.domain.workflow;

import java.util.UUID;

public record WorkflowStepDefinition(
        UUID id,
        String name,
        String approverRole,
        int orderIndex
) {
}
