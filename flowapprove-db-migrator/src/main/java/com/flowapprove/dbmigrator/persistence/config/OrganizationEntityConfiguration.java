package com.flowapprove.dbmigrator.persistence.config;

import java.util.List;

public class OrganizationEntityConfiguration {
    public String tableName() {
        return "flow_organizations";
    }

    public List<String> columns() {
        return List.of("id", "tenant_id", "name", "created_at");
    }
}
