package com.example.recycling_campaign_system.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_campaign_enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "campaign_id"}))
public class CampaignEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_enrollment_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false, foreignKey = @ForeignKey(name = "fk_enrollment_campaign"))
    private Campaign campaign;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDate enrolledAt;

    public CampaignEnrollment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDate enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
