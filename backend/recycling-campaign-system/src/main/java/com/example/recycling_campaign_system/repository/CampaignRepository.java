package com.example.recycling_campaign_system.repository;

import com.example.recycling_campaign_system.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

    List<Campaign> findAllByOrderByStartDateAsc();
}
