package com.flowapprove.application.workflowrequest.query;

import com.flowapprove.application.cqrs.QueryHandler;
import com.flowapprove.application.port.WorkflowRequestReadModelRepository;
import com.flowapprove.shared.error.DomainException;

public class GetWorkflowRequestDetailQueryHandler
        implements QueryHandler<GetWorkflowRequestDetailQuery, WorkflowRequestDetailView> {
    private final WorkflowRequestReadModelRepository workflowRequestReadModelRepository;

    public GetWorkflowRequestDetailQueryHandler(WorkflowRequestReadModelRepository workflowRequestReadModelRepository) {
        this.workflowRequestReadModelRepository = workflowRequestReadModelRepository;
    }

    @Override
    public Class<GetWorkflowRequestDetailQuery> queryType() {
        return GetWorkflowRequestDetailQuery.class;
    }

    @Override
    public WorkflowRequestDetailView handle(GetWorkflowRequestDetailQuery query) {
        return workflowRequestReadModelRepository.findViewById(query.workflowRequestId())
                .orElseThrow(() -> new DomainException("Workflow request not found."));
    }
}
