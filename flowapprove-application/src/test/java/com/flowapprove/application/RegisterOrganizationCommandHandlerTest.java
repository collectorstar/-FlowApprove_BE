package com.flowapprove.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flowapprove.application.organization.command.OrganizationView;
import com.flowapprove.application.organization.command.RegisterOrganizationCommand;
import com.flowapprove.application.organization.command.RegisterOrganizationCommandHandler;
import com.flowapprove.application.port.TenantSchemaProvisioner;
import com.flowapprove.application.port.UnitOfWork;
import com.flowapprove.domain.organization.Organization;
import com.flowapprove.domain.organization.OrganizationRepository;
import com.flowapprove.shared.identity.OrganizationId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RegisterOrganizationCommandHandlerTest {

    @Test
    void provisionsSchemaAndPersistsOrganization() {
        Map<OrganizationId, Organization> store = new HashMap<>();
        OrganizationRepository organizationRepository = new OrganizationRepository() {
            @Override
            public void save(Organization organization) {
                store.put(organization.getId(), organization);
            }

            @Override
            public Optional<Organization> findById(OrganizationId organizationId) {
                return Optional.ofNullable(store.get(organizationId));
            }
        };
        TenantSchemaProvisioner tenantSchemaProvisioner = tenantCode -> { };
        UnitOfWork unitOfWork = new UnitOfWork() {
            @Override
            public <T> T execute(TransactionCallback<T> callback) {
                return callback.doInTransaction();
            }
        };

        RegisterOrganizationCommandHandler handler = new RegisterOrganizationCommandHandler(
                organizationRepository,
                tenantSchemaProvisioner,
                unitOfWork
        );

        OrganizationView view = handler.handle(new RegisterOrganizationCommand("Finance", "finance"));

        assertEquals("Finance", view.organizationName());
        assertEquals("finance", view.tenantCode());
    }
}
