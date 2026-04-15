package com.flowapprove.dbmigrator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.flowapprove.dbmigrator.service.MigrationOrchestrator;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
class MigrationOrchestratorIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void provisionsAndMigratesTenantSchema() {
        MigrationOrchestrator orchestrator = new MigrationOrchestrator();
        DataSource dataSource = dataSource();

        orchestrator.run("provision", "finance", dataSource);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.tables where table_schema = 'tenant_finance' and table_name = 'workflow_requests'",
                Integer.class
        );

        assertTrue(count != null && count > 0);
    }

    private DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgres.getDriverClassName());
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        return dataSource;
    }
}
