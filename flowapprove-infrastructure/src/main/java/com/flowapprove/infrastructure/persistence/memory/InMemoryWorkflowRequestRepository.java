package com.flowapprove.infrastructure.persistence.memory;

import com.flowapprove.application.approval.query.PendingApprovalView;
import com.flowapprove.application.port.PendingApprovalReadModelRepository;
import com.flowapprove.application.port.WorkflowRequestReadModelRepository;
import com.flowapprove.application.workflowrequest.query.WorkflowRequestDetailView;
import com.flowapprove.domain.request.ApprovalTaskStatus;
import com.flowapprove.domain.request.WorkflowRequest;
import com.flowapprove.domain.request.WorkflowRequestRepository;
import com.flowapprove.shared.identity.UserId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWorkflowRequestRepository
        implements WorkflowRequestRepository, WorkflowRequestReadModelRepository, PendingApprovalReadModelRepository {
    private final Map<UUID, WorkflowRequest> store = new ConcurrentHashMap<>();

    @Override
    public void save(WorkflowRequest workflowRequest) {
        store.put(workflowRequest.getId(), workflowRequest);
    }

    @Override
    public Optional<WorkflowRequest> findById(UUID workflowRequestId) {
        return Optional.ofNullable(store.get(workflowRequestId));
    }

    @Override
    public Optional<WorkflowRequestDetailView> findViewById(UUID workflowRequestId) {
        return findById(workflowRequestId)
                .map(workflowRequest -> new WorkflowRequestDetailView(
                        workflowRequest.getId(),
                        workflowRequest.getWorkflowDefinitionId(),
                        workflowRequest.getOrganizationId().value(),
                        workflowRequest.getTenantId().value(),
                        workflowRequest.getRequesterId().value(),
                        workflowRequest.getTitle(),
                        workflowRequest.getStatus(),
                        workflowRequest.getSubmittedAt()
                ));
    }

    @Override
    public List<PendingApprovalView> findPendingApprovals() {
        return store.values().stream()
                .map(workflowRequest -> new PendingApprovalView(
                        UUID.randomUUID(),
                        workflowRequest.getId(),
                        "Manager Approval",
                        UserId.newId().value(),
                        ApprovalTaskStatus.PENDING
                ))
                .toList();
    }
}
