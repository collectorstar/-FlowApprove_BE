package com.flowapprove.application.workflowdefinition.command;

import com.flowapprove.domain.workflow.WorkflowDefinitionStatus;
import java.util.UUID;

public record WorkflowDefinitionView(UUID workflowDefinitionId, String name, WorkflowDefinitionStatus status, int version) {
}
