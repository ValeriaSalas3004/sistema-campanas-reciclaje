package com.example.recycling_campaign_system.Repository;

import com.example.recycling_campaign_system.Model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

    List<Campaign> findAllByOrderByStartDateAsc();
}
