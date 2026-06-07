package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.Campanas;
import com.example.recycling_campaign_system.Service.CampanasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
    public ResponseEntity<?> listCampanas(){
        return ResponseEntity.ok(this.service.findAll());
    }
    @GetMapping("/{id}")
    public Campanas findById(@PathVariable Integer id){
        return this.service.findById(id);
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
        if(campana==null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La campaña con id "+campana.getId()+ " ya esta registrada.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(campana);
    }

}
