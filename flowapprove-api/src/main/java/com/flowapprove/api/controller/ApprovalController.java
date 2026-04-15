package com.flowapprove.api.controller;

import com.flowapprove.application.approval.query.ListPendingApprovalsQuery;
import com.flowapprove.application.approval.query.PendingApprovalView;
import com.flowapprove.application.cqrs.QueryBus;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/approvals")
public class ApprovalController {
    private final QueryBus queryBus;

    public ApprovalController(QueryBus queryBus) {
        this.queryBus = queryBus;
    }

    @GetMapping("/pending")
    public List<PendingApprovalView> pending() {
        return queryBus.dispatch(new ListPendingApprovalsQuery());
    }
}
