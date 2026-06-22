package com.example.recycling_campaign_system.model.dto;

public class EnrollmentRequestDTO {

    private Integer userId;
    private Integer campaignId;

    public EnrollmentRequestDTO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
}
