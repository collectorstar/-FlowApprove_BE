package com.flowapprove.api.controller;

import com.flowapprove.application.cqrs.CommandBus;
import com.flowapprove.application.workflowdefinition.command.CreateWorkflowDefinitionCommand;
import com.flowapprove.application.workflowdefinition.command.PublishWorkflowDefinitionCommand;
import com.flowapprove.application.workflowdefinition.command.WorkflowDefinitionView;
import com.flowapprove.application.workflowdefinition.command.WorkflowStepInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workflow-definitions")
public class WorkflowDefinitionController {
    private final CommandBus commandBus;

    public WorkflowDefinitionController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping
    public WorkflowDefinitionView create(@Valid @RequestBody CreateWorkflowDefinitionRequest request) {
        return commandBus.dispatch(new CreateWorkflowDefinitionCommand(
                request.name(),
                request.steps().stream()
                        .map(step -> new WorkflowStepInput(step.name(), step.approverRole(), step.orderIndex()))
                        .toList()
        ));
    }

    @PostMapping("/{workflowDefinitionId}/publish")
    public WorkflowDefinitionView publish(@PathVariable UUID workflowDefinitionId) {
        return commandBus.dispatch(new PublishWorkflowDefinitionCommand(workflowDefinitionId));
    }

    public record CreateWorkflowDefinitionRequest(@NotBlank String name, @NotEmpty List<WorkflowStepRequest> steps) {
    }

    public record WorkflowStepRequest(@NotBlank String name, @NotBlank String approverRole, @NotNull Integer orderIndex) {
    }
}
