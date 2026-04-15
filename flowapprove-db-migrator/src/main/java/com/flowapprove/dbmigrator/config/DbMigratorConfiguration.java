package com.flowapprove.dbmigrator.config;

import com.flowapprove.dbmigrator.service.DbMigratorRunner;
import com.flowapprove.dbmigrator.service.EntityModelVersionValidator;
import com.flowapprove.dbmigrator.service.MigrationOrchestrator;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DbMigratorProperties.class)
public class DbMigratorConfiguration {

    @Bean
    public EntityModelVersionValidator entityModelVersionValidator() {
        return new EntityModelVersionValidator();
    }

    @Bean
    public MigrationOrchestrator migrationOrchestrator() {
        return new MigrationOrchestrator();
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            DbMigratorProperties properties,
            MigrationOrchestrator migrationOrchestrator,
            EntityModelVersionValidator validator,
            DataSource dataSource
    ) {
        return new DbMigratorRunner(properties, migrationOrchestrator, validator, dataSource);
    }
}
