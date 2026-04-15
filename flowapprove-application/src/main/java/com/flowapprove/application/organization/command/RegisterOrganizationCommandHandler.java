package com.flowapprove.application.organization.command;

import com.flowapprove.application.cqrs.CommandHandler;
import com.flowapprove.application.port.TenantSchemaProvisioner;
import com.flowapprove.application.port.UnitOfWork;
import com.flowapprove.domain.organization.Organization;
import com.flowapprove.domain.organization.OrganizationRepository;
import com.flowapprove.domain.organization.Tenant;
import com.flowapprove.shared.identity.OrganizationId;
import com.flowapprove.shared.identity.TenantId;

public class RegisterOrganizationCommandHandler implements CommandHandler<RegisterOrganizationCommand, OrganizationView> {
    private final OrganizationRepository organizationRepository;
    private final TenantSchemaProvisioner tenantSchemaProvisioner;
    private final UnitOfWork unitOfWork;

    public RegisterOrganizationCommandHandler(
            OrganizationRepository organizationRepository,
            TenantSchemaProvisioner tenantSchemaProvisioner,
            UnitOfWork unitOfWork
    ) {
        this.organizationRepository = organizationRepository;
        this.tenantSchemaProvisioner = tenantSchemaProvisioner;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public Class<RegisterOrganizationCommand> commandType() {
        return RegisterOrganizationCommand.class;
    }

    @Override
    public OrganizationView handle(RegisterOrganizationCommand command) {
        return unitOfWork.execute(() -> {
            Tenant tenant = new Tenant(TenantId.newId(), command.tenantCode(), "tenant_" + command.tenantCode());
            Organization organization = new Organization(OrganizationId.newId(), tenant.getId(), command.organizationName());
            organizationRepository.save(organization);
            tenantSchemaProvisioner.provision(command.tenantCode());
            return new OrganizationView(
                    organization.getId().value(),
                    tenant.getId().value(),
                    organization.getName(),
                    tenant.getTenantCode()
            );
        });
    }
}
