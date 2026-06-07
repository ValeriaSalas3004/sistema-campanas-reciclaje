package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.Campanas;
import com.example.recycling_campaign_system.Service.CampanasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/campaigns")
public class CampanasController {
    @Autowired
    private CampanasService service;

    @GetMapping
    public ResponseEntity<?> listCampanas() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        if(this.service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña con id "+id+" no existe");
        }
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/startDate")
    public ResponseEntity<?> sortByStarDate(){
        return ResponseEntity.ok(this.service.SortByStartDate());
    }

    @PostMapping
    public ResponseEntity<?> addCampana(@Validated @RequestBody Campanas campana, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        if (campana == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La campaña con id " + campana.getId() + " ya esta registrada.");
        }
        this.service.addCampana(campana);
        return ResponseEntity.status(HttpStatus.CREATED).body(campana);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCampana(@Validated @PathVariable Integer id, @RequestBody Campanas campana, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        if (this.service.editCampana(id,campana) == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("La campaña " + id + " no existe");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(campana);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCampana(@PathVariable Integer id) {
        if (this.service.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña " + id + " no existe");
        }
        this.service.deleteCampana(id);
        return ResponseEntity.ok("Campaña " + id + " a sido borrada");
    }

}
