package com.flowapprove.application.organization.command;

import com.flowapprove.application.cqrs.Command;

public record RegisterOrganizationCommand(String organizationName, String tenantCode) implements Command<OrganizationView> {
}
