package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.Campaign;
import com.example.recycling_campaign_system.model.CampaignEnrollment;
import com.example.recycling_campaign_system.model.User;
import com.example.recycling_campaign_system.model.dto.EnrollmentRequestDTO;
import com.example.recycling_campaign_system.service.CampaignEnrollmentService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CampaignEnrollmentController.class)
class CampaignEnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CampaignEnrollmentService service;

    @Test
    void enroll_upcomingCampaign_returnsCreated() throws Exception {
        EnrollmentRequestDTO dto = new EnrollmentRequestDTO();
        dto.setUserId(1);
        dto.setCampaignId(1);

        CampaignEnrollment enrollment = new CampaignEnrollment();
        enrollment.setId(1);
        enrollment.setUser(new User(1, "Ana", "ana@test.com", "pass", "voluntario"));
        Campaign campaign = new Campaign();
        campaign.setId(1);
        enrollment.setCampaign(campaign);
        enrollment.setEnrolledAt(LocalDate.now());

        when(service.enroll(1, 1)).thenReturn(CampaignEnrollmentService.EnrollResult.success(enrollment));

        mockMvc.perform(post("/enrollments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void enroll_campaignNotUpcoming_returnsConflict() throws Exception {
        EnrollmentRequestDTO dto = new EnrollmentRequestDTO();
        dto.setUserId(1);
        dto.setCampaignId(2);

        when(service.enroll(1, 2)).thenReturn(CampaignEnrollmentService.EnrollResult.campaignNotUpcoming());

        mockMvc.perform(post("/enrollments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void enroll_alreadyEnrolled_returnsConflict() throws Exception {
        EnrollmentRequestDTO dto = new EnrollmentRequestDTO();
        dto.setUserId(1);
        dto.setCampaignId(1);

        when(service.enroll(1, 1)).thenReturn(CampaignEnrollmentService.EnrollResult.alreadyEnrolled());

        mockMvc.perform(post("/enrollments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }
}
