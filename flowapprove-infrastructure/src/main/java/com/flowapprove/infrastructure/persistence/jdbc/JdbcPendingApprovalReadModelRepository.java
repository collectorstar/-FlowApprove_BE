package com.flowapprove.infrastructure.persistence.jdbc;

import com.flowapprove.application.approval.query.PendingApprovalView;
import com.flowapprove.application.port.PendingApprovalReadModelRepository;
import com.flowapprove.domain.request.ApprovalTaskStatus;
import java.util.List;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcPendingApprovalReadModelRepository implements PendingApprovalReadModelRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcPendingApprovalReadModelRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PendingApprovalView> findPendingApprovals() {
        String sql = """
                select id, workflow_request_id, step_name, assignee_id, status
                from approval_tasks
                where status = 'PENDING'
                order by created_at desc
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PendingApprovalView(
                rs.getObject("id", java.util.UUID.class),
                rs.getObject("workflow_request_id", java.util.UUID.class),
                rs.getString("step_name"),
                rs.getObject("assignee_id", java.util.UUID.class),
                ApprovalTaskStatus.valueOf(rs.getString("status"))
        ));
    }
}
