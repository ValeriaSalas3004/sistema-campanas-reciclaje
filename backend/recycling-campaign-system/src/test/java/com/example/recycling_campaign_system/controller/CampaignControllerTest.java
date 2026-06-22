package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.Campaign;
import com.example.recycling_campaign_system.service.CampaignService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CampaignController.class)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CampaignService service;

    @Test
    void addCampana_validDates_returnsCreated() throws Exception {
        Campaign campaign = new Campaign();
        campaign.setTitle("Campaña de verano");
        campaign.setDescription("Recolección en zonas costeras");
        campaign.setStartDate(LocalDate.of(2026, 1, 1));
        campaign.setEndDate(LocalDate.of(2026, 2, 1));

        mockMvc.perform(post("/campaigns")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(campaign)))
                .andExpect(status().isCreated());
    }

    @Test
    void addCampana_endDateBeforeStartDate_returnsBadRequest() throws Exception {
        Campaign campaign = new Campaign();
        campaign.setTitle("Campaña inválida");
        campaign.setDescription("Fechas invertidas");
        campaign.setStartDate(LocalDate.of(2026, 2, 1));
        campaign.setEndDate(LocalDate.of(2026, 1, 1));

        mockMvc.perform(post("/campaigns")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(campaign)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCampana_nonExistentId_returnsNotFound() throws Exception {
        when(service.findById(99)).thenReturn(null);

        mockMvc.perform(delete("/campaigns/99"))
                .andExpect(status().isNotFound());
    }
}
