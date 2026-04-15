package com.flowapprove.dbmigrator.persistence.config;

import java.util.List;

public class WorkflowDefinitionEntityConfiguration {
    public String tableName() {
        return "workflow_definitions";
    }

    public List<String> columns() {
        return List.of("id", "organization_id", "tenant_id", "name", "status", "version", "created_at");
    }
}
