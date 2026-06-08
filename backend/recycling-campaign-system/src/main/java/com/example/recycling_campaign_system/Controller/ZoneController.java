package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.RecollectionZone;
import com.example.recycling_campaign_system.Service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<RecollectionZone>> findAll() {
        return ResponseEntity.ok(zoneService.findAll());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<?> findByLocation(@PathVariable String location) {
        Optional<RecollectionZone> zone = zoneService.findByLocation(location);
        if (zone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(zone.get());
    }

    @GetMapping("/schedule/{schedule}")
    public ResponseEntity<?> findBySchedule(@PathVariable String schedule) {
        Optional<RecollectionZone> zone = zoneService.findBySchedule(schedule);
        if (zone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(zone.get());
    }

    @PostMapping
    public ResponseEntity<?> addZone(@Validated @RequestBody RecollectionZone zone) {
        Optional<RecollectionZone> added = zoneService.add(zone);
        if (added.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La zona de recolección ya existe o el ID es inválido.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(added.get());
    }

    @PutMapping
    public ResponseEntity<?> updateZone(@RequestBody RecollectionZone zone) {
        RecollectionZone updated = zoneService.update(zone);
        if (updated == null) {
            return ResponseEntity.badRequest()
                    .body("No se pudo actualizar. Asegúrate de enviar un ID válido.");
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        zoneService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody RecollectionZone zone) {
        zoneService.delete(zone);
        return ResponseEntity.ok().build();
    }
}