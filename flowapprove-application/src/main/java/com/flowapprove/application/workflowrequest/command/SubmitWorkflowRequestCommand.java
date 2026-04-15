package com.flowapprove.application.workflowrequest.command;

import com.flowapprove.application.cqrs.Command;
import java.util.UUID;

public record SubmitWorkflowRequestCommand(UUID workflowDefinitionId, String title)
        implements Command<WorkflowRequestSubmissionView> {
}
