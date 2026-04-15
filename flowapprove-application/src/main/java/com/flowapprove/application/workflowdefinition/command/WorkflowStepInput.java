package com.flowapprove.application.workflowdefinition.command;

public record WorkflowStepInput(String name, String approverRole, int orderIndex) {
}
