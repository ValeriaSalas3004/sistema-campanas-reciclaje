package com.example.recycling_campaign_system.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "reportDate", nullable = false)
    private LocalDate reportDate;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_user"))
    private User user;
    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_zone"))
    private RecollectionZone recoZone;
    @ManyToOne
    @JoinColumn(name = "waste_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_wasteType"))
    private WasteType wasteType;
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_campaign"))
    private Campaign campaign;

    public Report() {

    }

    public Report(Integer id, LocalDate reportDate, User user, RecollectionZone recoZone, WasteType wasteType, Campaign campaign) {
        this.id = id;
        this.reportDate = reportDate;
        this.user = user;
        this.recoZone = recoZone;
        this.wasteType = wasteType;
        this.campaign = campaign;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RecollectionZone getRecoZone() {
        return recoZone;
    }

    public void setRecoZone(RecollectionZone recoZone) {
        this.recoZone = recoZone;
    }

    public WasteType getWasteType() {
        return wasteType;
    }

    public void setWasteType(WasteType wasteType) {
        this.wasteType = wasteType;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
}