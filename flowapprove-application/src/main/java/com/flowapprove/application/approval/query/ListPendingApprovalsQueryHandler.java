package com.flowapprove.application.approval.query;

import com.flowapprove.application.cqrs.QueryHandler;
import com.flowapprove.application.port.PendingApprovalReadModelRepository;
import java.util.List;

public class ListPendingApprovalsQueryHandler
        implements QueryHandler<ListPendingApprovalsQuery, List<PendingApprovalView>> {
    private final PendingApprovalReadModelRepository pendingApprovalReadModelRepository;

    public ListPendingApprovalsQueryHandler(PendingApprovalReadModelRepository pendingApprovalReadModelRepository) {
        this.pendingApprovalReadModelRepository = pendingApprovalReadModelRepository;
    }

    @Override
    public Class<ListPendingApprovalsQuery> queryType() {
        return ListPendingApprovalsQuery.class;
    }

    @Override
    public List<PendingApprovalView> handle(ListPendingApprovalsQuery query) {
        return pendingApprovalReadModelRepository.findPendingApprovals();
    }
}
