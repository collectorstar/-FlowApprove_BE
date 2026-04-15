package com.flowapprove.application.approval.query;

import com.flowapprove.application.cqrs.Query;
import java.util.List;

public record ListPendingApprovalsQuery() implements Query<List<PendingApprovalView>> {
}
