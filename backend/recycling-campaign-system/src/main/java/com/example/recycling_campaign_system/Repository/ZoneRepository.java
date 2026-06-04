package com.example.recycling_campaign_system.Repository;

import com.example.recycling_campaign_system.Model.RecollectionZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<RecollectionZone,Long> {

    Optional<RecollectionZone> findByLocation(String location);

    Optional<RecollectionZone> findBySchedule(String schedule);

}
