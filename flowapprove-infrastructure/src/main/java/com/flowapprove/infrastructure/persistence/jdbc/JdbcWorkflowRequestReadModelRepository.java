package com.flowapprove.infrastructure.persistence.jdbc;

import com.flowapprove.application.port.WorkflowRequestReadModelRepository;
import com.flowapprove.application.workflowrequest.query.WorkflowRequestDetailView;
import com.flowapprove.domain.request.WorkflowRequestStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcWorkflowRequestReadModelRepository implements WorkflowRequestReadModelRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcWorkflowRequestReadModelRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<WorkflowRequestDetailView> findViewById(UUID workflowRequestId) {
        String sql = """
                select id, workflow_definition_id, organization_id, tenant_id, requester_id, title, status, submitted_at
                from workflow_requests
                where id = :id
                """;
        return jdbcTemplate.query(sql, new MapSqlParameterSource("id", workflowRequestId), rs -> rs.next()
                ? Optional.of(mapRow(rs))
                : Optional.empty());
    }

    private WorkflowRequestDetailView mapRow(ResultSet rs) throws SQLException {
        return new WorkflowRequestDetailView(
                rs.getObject("id", UUID.class),
                rs.getObject("workflow_definition_id", UUID.class),
                rs.getObject("organization_id", UUID.class),
                rs.getObject("tenant_id", UUID.class),
                rs.getObject("requester_id", UUID.class),
                rs.getString("title"),
                WorkflowRequestStatus.valueOf(rs.getString("status")),
                rs.getObject("submitted_at", Instant.class)
        );
    }
}
