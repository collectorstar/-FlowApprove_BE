package com.flowapprove.domain.organization;

import com.flowapprove.shared.identity.OrganizationId;
import java.util.Optional;

public interface OrganizationRepository {
    void save(Organization organization);

    Optional<Organization> findById(OrganizationId organizationId);
}
