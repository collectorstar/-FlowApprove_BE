package com.flowapprove.domain.request;

import com.flowapprove.domain.common.AggregateRoot;
import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;
import com.flowapprove.shared.identity.UserId;
import java.time.Instant;
import java.util.UUID;

public class WorkflowRequest extends AggregateRoot<UUID> {
    private final TenantId tenantId;
    private final OrganizationId organizationId;
    private final UUID workflowDefinitionId;
    private final UserId requesterId;
    private final String title;
    private final Instant submittedAt;
    private WorkflowRequestStatus status;

    public WorkflowRequest(
            UUID id,
            TenantId tenantId,
            OrganizationId organizationId,
            UUID workflowDefinitionId,
            UserId requesterId,
            String title,
            WorkflowRequestStatus status,
            Instant submittedAt
    ) {
        super(id);
        this.tenantId = tenantId;
        this.organizationId = organizationId;
        this.workflowDefinitionId = workflowDefinitionId;
        this.requesterId = requesterId;
        this.title = title;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public UUID getWorkflowDefinitionId() {
        return workflowDefinitionId;
    }

    public UserId getRequesterId() {
        return requesterId;
    }

    public String getTitle() {
        return title;
    }

    public WorkflowRequestStatus getStatus() {
        return status;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }
}
