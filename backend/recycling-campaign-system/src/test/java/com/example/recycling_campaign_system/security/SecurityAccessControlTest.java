package com.example.recycling_campaign_system.security;

import com.example.recycling_campaign_system.model.User;
import com.example.recycling_campaign_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAccessControlTest {

    private static final String GESTOR_EMAIL = "gestor.security.test@ucr.ac.cr";
    private static final String VOLUNTARIO_EMAIL = "voluntario.security.test@ucr.ac.cr";
    private static final String PASSWORD = "Secret123!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        createUserIfAbsent(GESTOR_EMAIL, "gestor");
        createUserIfAbsent(VOLUNTARIO_EMAIL, "voluntario");
    }

    private void createUserIfAbsent(String email, String role) {
        if (userRepository.findByEmail(email) != null) {
            return;
        }
        User user = new User();
        user.setName("Test " + role);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setRole(role);
        userRepository.save(user);
    }

    @Test
    void getCampaigns_anonymous_isOk() throws Exception {
        mockMvc.perform(get("/campaigns")).andExpect(status().isOk());
    }

    @Test
    void postCampaign_anonymous_isUnauthorized() throws Exception {
        mockMvc.perform(post("/campaigns").contentType("application/json").content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postCampaign_voluntario_isForbidden() throws Exception {
        mockMvc.perform(post("/campaigns")
                        .with(httpBasic(VOLUNTARIO_EMAIL, PASSWORD))
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postCampaign_gestor_isCreated() throws Exception {
        String body = """
                {"title":"Campaña seguridad","description":"test","startDate":"2026-01-01","endDate":"2026-02-01"}
                """;

        mockMvc.perform(post("/campaigns")
                        .with(httpBasic(GESTOR_EMAIL, PASSWORD))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void editOwnProfile_voluntario_isOk() throws Exception {
        User self = userRepository.findByEmail(VOLUNTARIO_EMAIL);
        String body = """
                {"name":"Nombre actualizado","email":"%s","role":"voluntario"}
                """.formatted(VOLUNTARIO_EMAIL);

        mockMvc.perform(put("/api/user/" + self.getId())
                        .with(httpBasic(VOLUNTARIO_EMAIL, PASSWORD))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void editOtherProfile_voluntario_isForbidden() throws Exception {
        User gestor = userRepository.findByEmail(GESTOR_EMAIL);
        String body = """
                {"name":"Hackeado","email":"%s","role":"voluntario"}
                """.formatted(GESTOR_EMAIL);

        mockMvc.perform(put("/api/user/" + gestor.getId())
                        .with(httpBasic(VOLUNTARIO_EMAIL, PASSWORD))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isForbidden());
    }
}
