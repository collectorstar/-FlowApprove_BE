package com.flowapprove.application.workflowdefinition.command;

import com.flowapprove.application.cqrs.Command;
import java.util.UUID;

public record PublishWorkflowDefinitionCommand(UUID workflowDefinitionId) implements Command<WorkflowDefinitionView> {
}
