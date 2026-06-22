package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.WasteType;
import com.example.recycling_campaign_system.service.WasteService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WasteController.class)
@AutoConfigureMockMvc(addFilters = false)
class WasteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WasteService wasteService;

    @Test
    void addWaste_newType_returnsCreated() throws Exception {
        WasteType waste = new WasteType();
        waste.setType("Plástico");
        waste.setWeight(10.5);

        when(wasteService.addWaste(any(WasteType.class))).thenReturn(Optional.of(waste));

        mockMvc.perform(post("/api/waste")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(waste)))
                .andExpect(status().isCreated());
    }

    @Test
    void addWaste_existingId_returnsConflict() throws Exception {
        WasteType waste = new WasteType();
        waste.setId(1L);
        waste.setType("Plástico");
        waste.setWeight(10.5);

        when(wasteService.addWaste(any(WasteType.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/waste")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(waste)))
                .andExpect(status().isConflict());
    }

    @Test
    void findById_nonExistentId_returnsNotFound() throws Exception {
        when(wasteService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/waste/id/99"))
                .andExpect(status().isNotFound());
    }
}
