package com.example.recycling_campaign_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name = "tb_recollection_zone")
public class RecollectionZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String location;

    @NotBlank
    private String schedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
