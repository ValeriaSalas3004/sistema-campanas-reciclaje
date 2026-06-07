package com.example.recycling_campaign_system.Repository;

import com.example.recycling_campaign_system.Model.Campanas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampanasRepository extends JpaRepository<Campanas, Integer> {


}
