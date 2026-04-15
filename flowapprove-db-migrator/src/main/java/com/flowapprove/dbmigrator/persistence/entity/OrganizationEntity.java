package com.flowapprove.dbmigrator.persistence.entity;

import java.util.UUID;

public record OrganizationEntity(UUID id, UUID tenantId, String name) {
}
