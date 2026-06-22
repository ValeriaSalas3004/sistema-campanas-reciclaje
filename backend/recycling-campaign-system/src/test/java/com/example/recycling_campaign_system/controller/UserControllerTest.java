package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.dto.UserLoginDTO;
import com.example.recycling_campaign_system.model.dto.UserRequestDTO;
import com.example.recycling_campaign_system.model.dto.UserResponseDTO;
import com.example.recycling_campaign_system.service.UserService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    @Test
    void saveUser_newEmail_returnsCreated() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Ana");
        dto.setEmail("ana@test.com");
        dto.setPassword("Passw0rd!");
        dto.setRole("voluntario");

        when(service.save(any(UserRequestDTO.class)))
                .thenReturn(new UserResponseDTO(1, "Ana", "ana@test.com", "voluntario"));

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void saveUser_duplicateEmail_returnsBadRequest() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Ana");
        dto.setEmail("ana@test.com");
        dto.setPassword("Passw0rd!");
        dto.setRole("voluntario");

        when(service.save(any(UserRequestDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_wrongCredentials_returnsUnauthorized() throws Exception {
        UserLoginDTO dto = new UserLoginDTO("ana@test.com", "Passw0rd!");

        when(service.login(any(UserLoginDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
