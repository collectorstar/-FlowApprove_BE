package com.flowapprove.dbmigrator.persistence.config;

import java.util.List;

public class WorkflowRequestEntityConfiguration {
    public String tableName() {
        return "workflow_requests";
    }

    public List<String> columns() {
        return List.of(
                "id",
                "workflow_definition_id",
                "organization_id",
                "tenant_id",
                "requester_id",
                "title",
                "status",
                "submitted_at"
        );
    }
}
