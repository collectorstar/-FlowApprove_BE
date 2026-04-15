package com.flowapprove.domain.workflow;

import com.flowapprove.domain.common.AggregateRoot;
import com.flowapprove.shared.error.DomainException;
import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WorkflowDefinition extends AggregateRoot<UUID> {
    private final TenantId tenantId;
    private final OrganizationId organizationId;
    private final String name;
    private final List<WorkflowStepDefinition> steps;
    private WorkflowDefinitionStatus status;
    private int version;

    public WorkflowDefinition(
            UUID id,
            TenantId tenantId,
            OrganizationId organizationId,
            String name,
            List<WorkflowStepDefinition> steps,
            WorkflowDefinitionStatus status,
            int version
    ) {
        super(id);
        this.tenantId = tenantId;
        this.organizationId = organizationId;
        this.name = name;
        this.steps = new ArrayList<>(steps);
        this.status = status;
        this.version = version;
    }

    public void publish() {
        if (steps.isEmpty()) {
            throw new DomainException("Workflow definition requires at least one step before publishing.");
        }
        this.status = WorkflowDefinitionStatus.PUBLISHED;
        this.version += 1;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public List<WorkflowStepDefinition> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public WorkflowDefinitionStatus getStatus() {
        return status;
    }

    public int getVersion() {
        return version;
    }
}
