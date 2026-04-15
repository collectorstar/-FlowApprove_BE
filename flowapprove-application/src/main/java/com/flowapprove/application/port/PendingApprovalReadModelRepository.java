package com.flowapprove.application.port;

import com.flowapprove.application.approval.query.PendingApprovalView;
import java.util.List;

public interface PendingApprovalReadModelRepository {
    List<PendingApprovalView> findPendingApprovals();
}
