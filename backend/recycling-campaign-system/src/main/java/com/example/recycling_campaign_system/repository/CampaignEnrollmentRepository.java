package com.example.recycling_campaign_system.repository;

import com.example.recycling_campaign_system.model.CampaignEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignEnrollmentRepository extends JpaRepository<CampaignEnrollment, Integer> {

    boolean existsByUserIdAndCampaignId(Integer userId, Integer campaignId);

    List<CampaignEnrollment> findByUserId(Integer userId);

    List<CampaignEnrollment> findByCampaignId(Integer campaignId);
}
