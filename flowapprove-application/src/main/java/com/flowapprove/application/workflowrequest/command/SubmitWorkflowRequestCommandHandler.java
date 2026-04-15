package com.flowapprove.application.workflowrequest.command;

import com.flowapprove.application.cqrs.CommandHandler;
import com.flowapprove.application.port.CurrentPrincipalProvider;
import com.flowapprove.application.port.UnitOfWork;
import com.flowapprove.domain.request.WorkflowRequest;
import com.flowapprove.domain.request.WorkflowRequestRepository;
import com.flowapprove.domain.request.WorkflowRequestStatus;
import java.time.Instant;
import java.util.UUID;

public class SubmitWorkflowRequestCommandHandler
        implements CommandHandler<SubmitWorkflowRequestCommand, WorkflowRequestSubmissionView> {
    private final WorkflowRequestRepository workflowRequestRepository;
    private final CurrentPrincipalProvider currentPrincipalProvider;
    private final UnitOfWork unitOfWork;

    public SubmitWorkflowRequestCommandHandler(
            WorkflowRequestRepository workflowRequestRepository,
            CurrentPrincipalProvider currentPrincipalProvider,
            UnitOfWork unitOfWork
    ) {
        this.workflowRequestRepository = workflowRequestRepository;
        this.currentPrincipalProvider = currentPrincipalProvider;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public Class<SubmitWorkflowRequestCommand> commandType() {
        return SubmitWorkflowRequestCommand.class;
    }

    @Override
    public WorkflowRequestSubmissionView handle(SubmitWorkflowRequestCommand command) {
        return unitOfWork.execute(() -> {
            var principal = currentPrincipalProvider.getCurrentPrincipal();
            WorkflowRequest workflowRequest = new WorkflowRequest(
                    UUID.randomUUID(),
                    principal.tenantId(),
                    principal.organizationId(),
                    command.workflowDefinitionId(),
                    principal.userId(),
                    command.title(),
                    WorkflowRequestStatus.SUBMITTED,
                    Instant.now()
            );
            workflowRequestRepository.save(workflowRequest);
            return new WorkflowRequestSubmissionView(
                    workflowRequest.getId(),
                    workflowRequest.getWorkflowDefinitionId(),
                    workflowRequest.getTitle(),
                    workflowRequest.getStatus(),
                    workflowRequest.getSubmittedAt()
            );
        });
    }
}
