package com.flowapprove.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flowapprove.infrastructure.logging.RequestTracingFilter;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void apiRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/approvals/pending"))
                .andExpect(header().exists(RequestTracingFilter.TRACE_ID_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedRequestCanAccessApi() throws Exception {
        mockMvc.perform(get("/api/v1/approvals/pending")
                        .with(jwt().jwt(jwt -> jwt
                                        .subject(UUID.randomUUID().toString())
                                        .claim("tenant", "finance")
                                        .claim("tenant_id", UUID.randomUUID().toString())
                                        .claim("org", UUID.randomUUID().toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_APPROVER"))))
                .andExpect(header().exists(RequestTracingFilter.TRACE_ID_HEADER))
                .andExpect(status().isOk());
    }

    @Test
    void keepsIncomingTraceId() throws Exception {
        mockMvc.perform(get("/api/v1/approvals/pending")
                        .header(RequestTracingFilter.TRACE_ID_HEADER, "trace-test-001")
                        .with(jwt().jwt(jwt -> jwt
                                        .subject(UUID.randomUUID().toString())
                                        .claim("tenant", "finance")
                                        .claim("tenant_id", UUID.randomUUID().toString())
                                        .claim("org", UUID.randomUUID().toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_APPROVER"))))
                .andExpect(header().string(RequestTracingFilter.TRACE_ID_HEADER, "trace-test-001"))
                .andExpect(status().isOk());
    }
}
