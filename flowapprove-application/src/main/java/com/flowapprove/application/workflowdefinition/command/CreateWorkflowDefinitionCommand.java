package com.flowapprove.application.workflowdefinition.command;

import com.flowapprove.application.cqrs.Command;
import java.util.List;

public record CreateWorkflowDefinitionCommand(String name, List<WorkflowStepInput> steps)
        implements Command<WorkflowDefinitionView> {
}
