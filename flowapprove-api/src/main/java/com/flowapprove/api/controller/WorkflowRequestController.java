package com.flowapprove.api.controller;

import com.flowapprove.application.cqrs.CommandBus;
import com.flowapprove.application.cqrs.QueryBus;
import com.flowapprove.application.workflowrequest.command.SubmitWorkflowRequestCommand;
import com.flowapprove.application.workflowrequest.command.WorkflowRequestSubmissionView;
import com.flowapprove.application.workflowrequest.query.GetWorkflowRequestDetailQuery;
import com.flowapprove.application.workflowrequest.query.WorkflowRequestDetailView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workflow-requests")
public class WorkflowRequestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public WorkflowRequestController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @PostMapping
    public WorkflowRequestSubmissionView submit(@Valid @RequestBody SubmitWorkflowRequestRequest request) {
        return commandBus.dispatch(new SubmitWorkflowRequestCommand(request.workflowDefinitionId(), request.title()));
    }

    @GetMapping("/{workflowRequestId}")
    public WorkflowRequestDetailView detail(@PathVariable UUID workflowRequestId) {
        return queryBus.dispatch(new GetWorkflowRequestDetailQuery(workflowRequestId));
    }

    public record SubmitWorkflowRequestRequest(@NotNull UUID workflowDefinitionId, @NotBlank String title) {
    }
}
