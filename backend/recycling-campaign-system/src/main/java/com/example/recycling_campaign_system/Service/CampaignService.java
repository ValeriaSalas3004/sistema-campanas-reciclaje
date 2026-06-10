package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.Campaign;
import com.example.recycling_campaign_system.Repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {
    @Autowired
    private CampaignRepository repo;

    public CampaignService() {
    }

    public Campaign addCampana(Campaign campana) {
        return this.repo.save(campana);
    }

    public void deleteCampana(Integer id) {
        this.repo.deleteById(id);
    }

    public Campaign editCampana(Integer id, Campaign campana) {
        Optional<Campaign> camp = this.repo.findById(id);
        if (camp.isPresent()) {
            Campaign campaign = camp.get();
            campaign = campana;
            return this.repo.save(campaign);
        }
        return null;
    }

    public List<Campaign> findAll() {
        return this.repo.findAll();
    }

    public Campaign findById(Integer id) {
        Optional<Campaign> opt = this.repo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public List<Campaign> SortByStartDate(){
        return this.repo.findAllByOrderByStartDateAsc();
    }
}
