package com.flowapprove.infrastructure.persistence.memory;

import com.flowapprove.domain.organization.Organization;
import com.flowapprove.domain.organization.OrganizationRepository;
import com.flowapprove.shared.identity.OrganizationId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrganizationRepository implements OrganizationRepository {
    private final Map<OrganizationId, Organization> store = new ConcurrentHashMap<>();

    @Override
    public void save(Organization organization) {
        store.put(organization.getId(), organization);
    }

    @Override
    public Optional<Organization> findById(OrganizationId organizationId) {
        return Optional.ofNullable(store.get(organizationId));
    }
}
