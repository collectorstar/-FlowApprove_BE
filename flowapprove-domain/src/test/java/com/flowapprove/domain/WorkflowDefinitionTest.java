package com.flowapprove.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flowapprove.domain.workflow.WorkflowDefinition;
import com.flowapprove.domain.workflow.WorkflowDefinitionStatus;
import com.flowapprove.domain.workflow.WorkflowStepDefinition;
import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WorkflowDefinitionTest {

    @Test
    void publishAdvancesVersionAndStatus() {
        WorkflowDefinition workflowDefinition = new WorkflowDefinition(
                UUID.randomUUID(),
                TenantId.newId(),
                OrganizationId.newId(),
                "Travel request",
                List.of(new WorkflowStepDefinition(UUID.randomUUID(), "Manager Approval", "APPROVER", 1)),
                WorkflowDefinitionStatus.DRAFT,
                0
        );

        workflowDefinition.publish();

        assertEquals(WorkflowDefinitionStatus.PUBLISHED, workflowDefinition.getStatus());
        assertEquals(1, workflowDefinition.getVersion());
    }
}
