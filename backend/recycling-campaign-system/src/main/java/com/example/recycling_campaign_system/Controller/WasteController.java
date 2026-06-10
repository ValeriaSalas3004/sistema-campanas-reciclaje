package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.WasteType;
import com.example.recycling_campaign_system.Service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/waste")
public class WasteController {

    @Autowired
    private WasteService wasteService;

    @GetMapping
    public ResponseEntity<List<WasteType>> findAll() {
        return ResponseEntity.ok(wasteService.findAll());
    }

    @GetMapping("/exists/type/{type}")
    public ResponseEntity<Boolean> existsByType(@PathVariable String type) {
        return ResponseEntity.ok(wasteService.existsByType(type));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<WasteType> waste = wasteService.findById(id);
        if (waste.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(waste.get());
    }

    @GetMapping("/weight/{weight}")
    public ResponseEntity<?> findByWeight(@PathVariable Double weight) {
        Optional<WasteType> waste = wasteService.findByWeight(weight);
        if (waste.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(waste.get());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> findByType(@PathVariable String type) {
        Optional<WasteType> waste = wasteService.findByType(type);
        if (waste.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(waste.get());
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody WasteType waste) {
        WasteType updated = wasteService.update(waste);
        if (updated == null) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el registro.");
        }
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<?> addWaste(@RequestBody WasteType wasteType) {
        Optional<WasteType> added = wasteService.addWaste(wasteType);
        if (added.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El residuo ya existe o falló la creación.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(added.get());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        wasteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody WasteType entity) {
        wasteService.delete(entity);
        return ResponseEntity.ok().build();
    }
}