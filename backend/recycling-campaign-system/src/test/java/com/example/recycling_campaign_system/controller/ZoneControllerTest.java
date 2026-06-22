package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.RecollectionZone;
import com.example.recycling_campaign_system.service.ZoneService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ZoneController.class)
class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ZoneService zoneService;

    @Test
    void addZone_newZone_returnsCreated() throws Exception {
        RecollectionZone zone = new RecollectionZone();
        zone.setLocation("Centro");
        zone.setSchedule("Lunes 8am-12pm");

        when(zoneService.add(any(RecollectionZone.class))).thenReturn(Optional.of(zone));

        mockMvc.perform(post("/api/zones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(zone)))
                .andExpect(status().isCreated());
    }

    @Test
    void addZone_existingId_returnsConflict() throws Exception {
        RecollectionZone zone = new RecollectionZone();
        zone.setId(1L);
        zone.setLocation("Centro");
        zone.setSchedule("Lunes 8am-12pm");

        when(zoneService.add(any(RecollectionZone.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/zones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(zone)))
                .andExpect(status().isConflict());
    }

    @Test
    void findByLocation_nonExistentLocation_returnsNotFound() throws Exception {
        when(zoneService.findByLocation("Inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/zones/location/Inexistente"))
                .andExpect(status().isNotFound());
    }
}
