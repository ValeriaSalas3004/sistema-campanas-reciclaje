package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.RecollectionZone;
import com.example.recycling_campaign_system.Repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository repository;

    public Optional<RecollectionZone> add(RecollectionZone zone) {
        if (zone.getId() != null && repository.existsById(zone.getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(zone));
    }

    public RecollectionZone update(RecollectionZone zone) {
        RecollectionZone newZone = repository.findById(zone.getId()).orElse(null);

        if (newZone != null) {

            if (zone.getLocation() != null
                    && !Objects.equals(zone.getLocation(), "")
                    && !Objects.equals(zone.getLocation(), " ")) {
                newZone.setLocation(zone.getLocation());
            }

            if (zone.getSchedule() != null
                    && !Objects.equals(zone.getLocation(), "")
                    && !Objects.equals(zone.getLocation(), " ")) {
                newZone.setSchedule(zone.getSchedule());
            }

            return repository.save(newZone);
        }
        return null;
    }

    public void delete(RecollectionZone zone) {
        repository.delete(zone);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<RecollectionZone> findByLocation(String location) {
        return repository.findByLocation(location);
    }

    public Optional<RecollectionZone> findBySchedule(String schedule) {
        return repository.findBySchedule(schedule);
    }

    public List<RecollectionZone> findAll() {
        return repository.findAll();
    }


}
