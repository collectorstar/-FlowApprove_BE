package com.flowapprove.api.controller;

import com.flowapprove.application.cqrs.CommandBus;
import com.flowapprove.application.organization.command.OrganizationView;
import com.flowapprove.application.organization.command.RegisterOrganizationCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
    private final CommandBus commandBus;

    public OrganizationController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping
    public OrganizationView registerOrganization(@Valid @RequestBody RegisterOrganizationRequest request) {
        return commandBus.dispatch(new RegisterOrganizationCommand(request.organizationName(), request.tenantCode()));
    }

    public record RegisterOrganizationRequest(@NotBlank String organizationName, @NotBlank String tenantCode) {
    }
}
