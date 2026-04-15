package com.flowapprove.application.workflowdefinition.command;

import com.flowapprove.application.cqrs.CommandHandler;
import com.flowapprove.application.port.UnitOfWork;
import com.flowapprove.domain.workflow.WorkflowDefinition;
import com.flowapprove.domain.workflow.WorkflowDefinitionRepository;
import com.flowapprove.shared.error.DomainException;

public class PublishWorkflowDefinitionCommandHandler
        implements CommandHandler<PublishWorkflowDefinitionCommand, WorkflowDefinitionView> {
    private final WorkflowDefinitionRepository workflowDefinitionRepository;
    private final UnitOfWork unitOfWork;

    public PublishWorkflowDefinitionCommandHandler(
            WorkflowDefinitionRepository workflowDefinitionRepository,
            UnitOfWork unitOfWork
    ) {
        this.workflowDefinitionRepository = workflowDefinitionRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public Class<PublishWorkflowDefinitionCommand> commandType() {
        return PublishWorkflowDefinitionCommand.class;
    }

    @Override
    public WorkflowDefinitionView handle(PublishWorkflowDefinitionCommand command) {
        return unitOfWork.execute(() -> {
            WorkflowDefinition workflowDefinition = workflowDefinitionRepository.findById(command.workflowDefinitionId())
                    .orElseThrow(() -> new DomainException("Workflow definition not found."));
            workflowDefinition.publish();
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
