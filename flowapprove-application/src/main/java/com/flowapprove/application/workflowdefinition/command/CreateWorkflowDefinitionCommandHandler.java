package com.flowapprove.application.workflowdefinition.command;

import com.flowapprove.application.cqrs.CommandHandler;
import com.flowapprove.application.port.CurrentPrincipalProvider;
import com.flowapprove.application.port.UnitOfWork;
import com.flowapprove.domain.workflow.WorkflowDefinition;
import com.flowapprove.domain.workflow.WorkflowDefinitionRepository;
import com.flowapprove.domain.workflow.WorkflowDefinitionStatus;
import com.flowapprove.domain.workflow.WorkflowStepDefinition;
import java.util.List;
import java.util.UUID;

public class CreateWorkflowDefinitionCommandHandler
        implements CommandHandler<CreateWorkflowDefinitionCommand, WorkflowDefinitionView> {
    private final WorkflowDefinitionRepository workflowDefinitionRepository;
    private final CurrentPrincipalProvider currentPrincipalProvider;
    private final UnitOfWork unitOfWork;

    public CreateWorkflowDefinitionCommandHandler(
            WorkflowDefinitionRepository workflowDefinitionRepository,
            CurrentPrincipalProvider currentPrincipalProvider,
            UnitOfWork unitOfWork
    ) {
        this.workflowDefinitionRepository = workflowDefinitionRepository;
        this.currentPrincipalProvider = currentPrincipalProvider;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public Class<CreateWorkflowDefinitionCommand> commandType() {
        return CreateWorkflowDefinitionCommand.class;
    }

    @Override
    public WorkflowDefinitionView handle(CreateWorkflowDefinitionCommand command) {
        return unitOfWork.execute(() -> {
            var principal = currentPrincipalProvider.getCurrentPrincipal();
            List<WorkflowStepDefinition> steps = command.steps().stream()
                    .map(step -> new WorkflowStepDefinition(UUID.randomUUID(), step.name(), step.approverRole(), step.orderIndex()))
                    .toList();
            WorkflowDefinition workflowDefinition = new WorkflowDefinition(
                    UUID.randomUUID(),
                    principal.tenantId(),
                    principal.organizationId(),
                    command.name(),
                    steps,
                    WorkflowDefinitionStatus.DRAFT,
                    0
            );
            workflowDefinitionRepository.save(workflowDefinition);
            return new WorkflowDefinitionView(
                    workflowDefinition.getId(),
                    workflowDefinition.getName(),
                    workflowDefinition.getStatus(),
                    workflowDefinition.getVersion()
            );
        });
    }
}
