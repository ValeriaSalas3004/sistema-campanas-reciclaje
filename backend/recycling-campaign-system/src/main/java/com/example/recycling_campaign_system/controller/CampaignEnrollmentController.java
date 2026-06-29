package com.example.recycling_campaign_system.controller;

import com.example.recycling_campaign_system.model.dto.EnrollmentRequestDTO;
import com.example.recycling_campaign_system.service.CampaignEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/enrollments")
public class CampaignEnrollmentController {

    @Autowired
    private CampaignEnrollmentService service;

    @PostMapping
    public ResponseEntity<?> enroll(@RequestBody EnrollmentRequestDTO dto) {
        CampaignEnrollmentService.EnrollResult result = service.enroll(dto.getUserId(), dto.getCampaignId());

        switch (result.getStatus()) {
            case USER_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario indicado no existe");
            case CAMPAIGN_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña indicada no existe");
            case CAMPAIGN_NOT_UPCOMING:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Solo puedes inscribirte a campañas próximas a realizarse");
            case ALREADY_ENROLLED:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya estás inscrito en esta campaña");
            default:
                return ResponseEntity.status(HttpStatus.CREATED).body(result.getEnrollment());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unenroll(@PathVariable Integer id) {
        if (!service.unenroll(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La inscripción con ID " + id + " no existe");
        }
        return ResponseEntity.ok("Inscripción cancelada");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<?> findByCampaign(@PathVariable Integer campaignId) {
        return ResponseEntity.ok(service.findByCampaign(campaignId));
    }
}
