package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Repository.CampanasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampanasService {
    @Autowired
    private CampanasRepository repo;

    public CampanasService() {
    }
}
