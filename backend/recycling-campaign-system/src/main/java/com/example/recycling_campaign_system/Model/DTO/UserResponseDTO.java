package com.example.recycling_campaign_system.Model.DTO;

public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;
    private String role;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Integer id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
