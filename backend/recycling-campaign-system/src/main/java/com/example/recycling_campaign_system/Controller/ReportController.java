package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.Report;
import com.example.recycling_campaign_system.Service.ReportService;
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
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService service;


    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdReport(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.findByIdReport(id));
    }

    @GetMapping("/campaing/{campaignId}")
    public ResponseEntity<?> findByCampaignId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.findByCampaignId(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.findByUserid(id));
    }

    @GetMapping("/zone/{zoneId")
    public ResponseEntity<?> findByZoneId(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.findByZoneId(id));
    }

    @GetMapping("/waste/{wasteTypeId")
    public ResponseEntity<?> findByWasteId(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.findByWasteId(id));
    }

    @PostMapping
    public ResponseEntity<?> addReport(@Validated @RequestBody Report report, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }


        if (report == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El reporte no puede estar vacio");
        }


        Report newReport = this.service.addReport(report);
        if (newReport == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear el reporte porfavor verifique los datos ingresados");
        }
        return ResponseEntity.status(HttpStatus.CREATED).

                body(newReport);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editReport(@PathVariable Integer id, @RequestBody Report report, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Report r = this.service.editReport(id, report);
        if (r == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No existe un reporte con el ID " + r.getId());

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Integer id) {
        if (service.findByIdReport(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El reporte con ID " + id + " no existe");
        }
        this.service.deleteReport(id);
        return ResponseEntity.ok("El reporte a sido eliminado");
    }
}
