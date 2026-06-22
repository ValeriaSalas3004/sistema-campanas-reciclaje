package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.*;
import com.example.recycling_campaign_system.service.ReportService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReportService service;

    private Report buildReport() {
        Report report = new Report();
        report.setReportDate(LocalDate.of(2026, 3, 1));
        report.setUser(new User(1, "Ana", "ana@test.com", "pass", "voluntario"));
        RecollectionZone zone = new RecollectionZone();
        zone.setId(1L);
        report.setRecoZone(zone);
        WasteType waste = new WasteType();
        waste.setId(1L);
        report.setWasteType(waste);
        Campaign campaign = new Campaign();
        campaign.setId(1);
        report.setCampaign(campaign);
        return report;
    }

    @Test
    void addReport_validForeignKeys_returnsCreated() throws Exception {
        Report report = buildReport();
        when(service.addReport(any(Report.class))).thenReturn(report);

        mockMvc.perform(post("/reports")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isCreated());
    }

    @Test
    void addReport_missingForeignKey_returnsConflict() throws Exception {
        Report report = buildReport();
        when(service.addReport(any(Report.class))).thenReturn(null);

        mockMvc.perform(post("/reports")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isConflict());
    }

    @Test
    void editReport_nonExistentId_returnsNoContent() throws Exception {
        Report report = buildReport();
        when(service.editReport(eq(99), any(Report.class))).thenReturn(null);

        mockMvc.perform(put("/reports/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isNoContent());
    }
}
