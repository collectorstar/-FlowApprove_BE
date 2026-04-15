package com.flowapprove.dbmigrator.service;

import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;

public class MigrationOrchestrator {

    public void run(String action, String tenant, DataSource dataSource) {
        migratePublicSchema(dataSource);
        if ("provision".equalsIgnoreCase(action)) {
            if ("all".equalsIgnoreCase(tenant)) {
                throw new IllegalArgumentException("Provision requires a concrete tenant code.");
            }
            provisionTenant(tenant, dataSource);
            migrateTenantSchema(schemaNameFor(tenant), dataSource);
            return;
        }

        if ("all".equalsIgnoreCase(tenant)) {
            for (String schema : findAllTenantSchemas(dataSource)) {
                migrateTenantSchema(schema, dataSource);
            }
            return;
        }

        migrateTenantSchema(schemaNameFor(tenant), dataSource);
    }

    public void provisionTenant(String tenantCode, DataSource dataSource) {
        String schemaName = schemaNameFor(tenantCode);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("create schema if not exists " + schemaName);
        jdbcTemplate.update("""
                insert into public.flow_tenants(id, tenant_code, schema_name)
                values (?, ?, ?)
                on conflict (tenant_code) do update set schema_name = excluded.schema_name
                """, UUID.randomUUID(), tenantCode, schemaName);
        jdbcTemplate.update("""
                insert into public.flow_organizations(id, tenant_id, name)
                select ?, id, ?
                from public.flow_tenants
                where tenant_code = ?
                on conflict do nothing
                """, UUID.randomUUID(), tenantCode + " organization", tenantCode);
    }

    private void migratePublicSchema(DataSource dataSource) {
        Flyway.configure()
                .dataSource(dataSource)
                .schemas("public")
                .locations("classpath:db/migration/public")
                .load()
                .migrate();
    }

    private void migrateTenantSchema(String schemaName, DataSource dataSource) {
        Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .defaultSchema(schemaName)
                .locations("classpath:db/migration/tenant")
                .load()
                .migrate();
    }

    private List<String> findAllTenantSchemas(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "select schema_name from public.flow_tenants order by tenant_code",
                (rs, rowNum) -> rs.getString("schema_name")
        );
    }

    private String schemaNameFor(String tenantCode) {
        return "tenant_" + tenantCode.trim().toLowerCase().replace('-', '_');
    }
}
