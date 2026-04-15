package com.flowapprove.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().isOk());
    }
}
