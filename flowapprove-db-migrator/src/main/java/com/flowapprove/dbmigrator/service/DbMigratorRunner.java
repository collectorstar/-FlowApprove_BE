package com.flowapprove.dbmigrator.service;

import com.flowapprove.dbmigrator.config.DbMigratorProperties;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;

public class DbMigratorRunner implements CommandLineRunner {
    private final DbMigratorProperties properties;
    private final MigrationOrchestrator migrationOrchestrator;
    private final EntityModelVersionValidator validator;
    private final DataSource dataSource;

    public DbMigratorRunner(
            DbMigratorProperties properties,
            MigrationOrchestrator migrationOrchestrator,
            EntityModelVersionValidator validator,
            DataSource dataSource
    ) {
        this.properties = properties;
        this.migrationOrchestrator = migrationOrchestrator;
        this.validator = validator;
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        if (!properties.isEnabled()) {
            return;
        }
        validator.validate();
        migrationOrchestrator.run(properties.getAction(), properties.getTenant(), dataSource);
    }
}
