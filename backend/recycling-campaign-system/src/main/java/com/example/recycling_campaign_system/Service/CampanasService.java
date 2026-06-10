package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.Campanas;
import com.example.recycling_campaign_system.Repository.CampanasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampanasService {
    @Autowired
    private CampanasRepository repo;

    public CampanasService() {
    }

    public Campanas addCampana(Campanas campana) {
        return this.repo.save(campana);
    }

    public void deleteCampana(Integer id) {
        this.repo.deleteById(id);
    }

    public Campanas editCampana(Integer id, Campanas campana) {
        Optional<Campanas> camp = this.repo.findById(id);
        if (camp.isPresent()) {
            Campanas campanas = camp.get();
            campanas = campana;
            return this.repo.save(campanas);
        }
        return null;
    }

    public List<Campanas> findAll() {
        return this.repo.findAll();
    }

    public Campanas findById(Integer id) {
        Optional<Campanas> opt = this.repo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public List<Campanas> SortByStartDate(){
        return this.repo.findAllByOrderByStartDateAsc();
    }
}
