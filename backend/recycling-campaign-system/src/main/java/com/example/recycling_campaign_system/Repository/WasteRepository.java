package com.example.recycling_campaign_system.Repository;

import com.example.recycling_campaign_system.Model.WasteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WasteRepository extends JpaRepository<WasteType, Long> {

    Optional<WasteType> findByWeight(Double weight);

    Optional<WasteType> findByType(String type);

}
