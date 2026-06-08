package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.WasteType;
import com.example.recycling_campaign_system.Repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WasteService {

    @Autowired
    private WasteRepository repository;

    public WasteType update(WasteType waste) {
        WasteType newWaste = repository.findById(waste.getId()).orElse(null);

        if (newWaste != null) {

            if (newWaste.getType() != null
                    && !Objects.equals(waste.getType(), "")
                    && !Objects.equals(waste.getType(), " ")) {
                newWaste.setType(waste.getType());
            }

            if (newWaste.getWeight() != null){
                newWaste.setWeight(waste.getWeight());
            }

            repository.save(newWaste);
        }
        return null;
    }

    public Optional<WasteType> addWaste(WasteType wasteType) {
        if (wasteType.getId() !=null && repository.existsById(wasteType.getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(wasteType));
    }

    public Boolean existsByType(String type) {
        return repository.existsByType(type);
    }

    public Optional<WasteType> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<WasteType> findByWeight(Double weight) {
        return repository.findByWeight(weight);
    }

    public Optional<WasteType> findByType(String type) {
        return repository.findByType(type);
    }

    public List<WasteType> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    public void delete(WasteType entity) {
        repository.delete(entity);
    }
}
