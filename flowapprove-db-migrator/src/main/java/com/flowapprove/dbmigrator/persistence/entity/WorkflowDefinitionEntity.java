package com.flowapprove.dbmigrator.persistence.entity;

import java.util.UUID;

public record WorkflowDefinitionEntity(UUID id, UUID organizationId, UUID tenantId, String name, String status, int version) {
}
