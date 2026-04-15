package com.flowapprove.infrastructure.config;

import com.flowapprove.application.approval.query.ListPendingApprovalsQueryHandler;
import com.flowapprove.application.cqrs.CommandBus;
import com.flowapprove.application.cqrs.CommandHandler;
import com.flowapprove.application.cqrs.QueryBus;
import com.flowapprove.application.cqrs.QueryHandler;
import com.flowapprove.application.organization.command.RegisterOrganizationCommandHandler;
import com.flowapprove.application.port.CurrentPrincipalProvider;
import com.flowapprove.application.port.PendingApprovalReadModelRepository;
import com.flowapprove.application.port.TenantSchemaProvisioner;
import com.flowapprove.application.port.UnitOfWork;
import com.flowapprove.application.port.WorkflowRequestReadModelRepository;
import com.flowapprove.application.workflowdefinition.command.CreateWorkflowDefinitionCommandHandler;
import com.flowapprove.application.workflowdefinition.command.PublishWorkflowDefinitionCommandHandler;
import com.flowapprove.application.workflowrequest.command.SubmitWorkflowRequestCommandHandler;
import com.flowapprove.application.workflowrequest.query.GetWorkflowRequestDetailQueryHandler;
import com.flowapprove.domain.organization.OrganizationRepository;
import com.flowapprove.domain.request.WorkflowRequestRepository;
import com.flowapprove.domain.workflow.WorkflowDefinitionRepository;
import com.flowapprove.infrastructure.cqrs.SimpleCommandBus;
import com.flowapprove.infrastructure.cqrs.SimpleQueryBus;
import com.flowapprove.infrastructure.persistence.memory.InMemoryOrganizationRepository;
import com.flowapprove.infrastructure.persistence.memory.InMemoryWorkflowDefinitionRepository;
import com.flowapprove.infrastructure.persistence.memory.InMemoryWorkflowRequestRepository;
import com.flowapprove.infrastructure.redis.config.RedisProperties;
import com.flowapprove.infrastructure.redis.key.RedisKeyRegistry;
import com.flowapprove.infrastructure.redis.service.FailOpenRedisCommandExecutor;
import com.flowapprove.infrastructure.redis.service.RedisAvailabilityGuard;
import com.flowapprove.infrastructure.redis.service.RedisCacheService;
import com.flowapprove.infrastructure.redis.service.RedisCommandExecutor;
import com.flowapprove.infrastructure.security.SpringSecurityCurrentPrincipalProvider;
import com.flowapprove.infrastructure.tenant.TenantContextFilter;
import com.flowapprove.infrastructure.tenant.TenantContextHolder;
import com.flowapprove.infrastructure.tenant.TenantSchemaResolver;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class InfrastructureConfiguration {

    @Bean
    public TenantSchemaResolver tenantSchemaResolver() {
        return new TenantSchemaResolver();
    }

    @Bean
    public TenantContextFilter tenantContextFilter(TenantSchemaResolver tenantSchemaResolver) {
        return new TenantContextFilter(tenantSchemaResolver, true);
    }

    @Bean
    public TenantSchemaProvisioner tenantSchemaProvisioner(TenantSchemaResolver tenantSchemaResolver) {
        return tenantCode -> TenantContextHolder.set(new com.flowapprove.infrastructure.tenant.TenantContext(
                tenantCode,
                tenantSchemaResolver.resolveSchemaName(tenantCode)
        ));
    }

    @Bean
    public CurrentPrincipalProvider currentPrincipalProvider() {
        return new SpringSecurityCurrentPrincipalProvider();
    }

    @Bean
    public UnitOfWork unitOfWork() {
        return new UnitOfWork() {
            @Override
            public <T> T execute(TransactionCallback<T> callback) {
                return callback.doInTransaction();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public InMemoryWorkflowRequestRepository inMemoryWorkflowRequestRepository() {
        return new InMemoryWorkflowRequestRepository();
    }

    @Bean
    public OrganizationRepository organizationRepository() {
        return new InMemoryOrganizationRepository();
    }

    @Bean
    public WorkflowDefinitionRepository workflowDefinitionRepository() {
        return new InMemoryWorkflowDefinitionRepository();
    }

    @Bean
    public WorkflowRequestRepository workflowRequestRepository(
            @Qualifier("inMemoryWorkflowRequestRepository") InMemoryWorkflowRequestRepository repository
    ) {
        return repository;
    }

    @Bean
    public WorkflowRequestReadModelRepository workflowRequestReadModelRepository(
            @Qualifier("inMemoryWorkflowRequestRepository") InMemoryWorkflowRequestRepository repository
    ) {
        return repository;
    }

    @Bean
    public PendingApprovalReadModelRepository pendingApprovalReadModelRepository(
            @Qualifier("inMemoryWorkflowRequestRepository") InMemoryWorkflowRequestRepository repository
    ) {
        return repository;
    }

    @Bean
    public RegisterOrganizationCommandHandler registerOrganizationCommandHandler(
            OrganizationRepository organizationRepository,
            TenantSchemaProvisioner tenantSchemaProvisioner,
            UnitOfWork unitOfWork
    ) {
        return new RegisterOrganizationCommandHandler(organizationRepository, tenantSchemaProvisioner, unitOfWork);
    }

    @Bean
    public CreateWorkflowDefinitionCommandHandler createWorkflowDefinitionCommandHandler(
            WorkflowDefinitionRepository workflowDefinitionRepository,
            CurrentPrincipalProvider currentPrincipalProvider,
            UnitOfWork unitOfWork
    ) {
        return new CreateWorkflowDefinitionCommandHandler(workflowDefinitionRepository, currentPrincipalProvider, unitOfWork);
    }

    @Bean
    public PublishWorkflowDefinitionCommandHandler publishWorkflowDefinitionCommandHandler(
            WorkflowDefinitionRepository workflowDefinitionRepository,
            UnitOfWork unitOfWork
    ) {
        return new PublishWorkflowDefinitionCommandHandler(workflowDefinitionRepository, unitOfWork);
    }

    @Bean
    public SubmitWorkflowRequestCommandHandler submitWorkflowRequestCommandHandler(
            WorkflowRequestRepository workflowRequestRepository,
            CurrentPrincipalProvider currentPrincipalProvider,
            UnitOfWork unitOfWork
    ) {
        return new SubmitWorkflowRequestCommandHandler(workflowRequestRepository, currentPrincipalProvider, unitOfWork);
    }

    @Bean
    public GetWorkflowRequestDetailQueryHandler getWorkflowRequestDetailQueryHandler(
            WorkflowRequestReadModelRepository workflowRequestReadModelRepository
    ) {
        return new GetWorkflowRequestDetailQueryHandler(workflowRequestReadModelRepository);
    }

    @Bean
    public ListPendingApprovalsQueryHandler listPendingApprovalsQueryHandler(
            PendingApprovalReadModelRepository pendingApprovalReadModelRepository
    ) {
        return new ListPendingApprovalsQueryHandler(pendingApprovalReadModelRepository);
    }

    @Bean
    public CommandBus commandBus(List<CommandHandler<?, ?>> handlers) {
        return new SimpleCommandBus(handlers);
    }

    @Bean
    public QueryBus queryBus(List<QueryHandler<?, ?>> handlers) {
        return new SimpleQueryBus(handlers);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort()
        );
        standaloneConfiguration.setDatabase(redisProperties.getDatabase());
        if (redisProperties.getPassword() != null && !redisProperties.getPassword().isBlank()) {
            standaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }

        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(redisProperties.getCommandTimeout())
                .build();

        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisAvailabilityGuard redisAvailabilityGuard() {
        return new RedisAvailabilityGuard(java.time.Duration.ofSeconds(30));
    }

    @Bean
    public RedisCommandExecutor redisCommandExecutor(RedisProperties redisProperties, RedisAvailabilityGuard redisAvailabilityGuard) {
        return new FailOpenRedisCommandExecutor(redisProperties.isEnabled(), redisAvailabilityGuard);
    }

    @Bean
    public RedisKeyRegistry redisKeyRegistry(RedisProperties redisProperties) {
        return new RedisKeyRegistry(redisProperties);
    }

    @Bean
    public RedisCacheService redisCacheService(
            StringRedisTemplate stringRedisTemplate,
            RedisCommandExecutor redisCommandExecutor,
            RedisProperties redisProperties
    ) {
        return new RedisCacheService(stringRedisTemplate, redisCommandExecutor, redisProperties.getDefaultTtl());
    }
}
