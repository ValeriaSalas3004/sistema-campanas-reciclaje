package com.example.recycling_campaign_system.Repository;

import com.example.recycling_campaign_system.Model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByCampaignId(Integer Id);

    List<Report> findByUserId(Integer id);

    List<Report> findByRecoZoneId(Long id);

    List<Report> findByWasteTypeId(Long id);
}
