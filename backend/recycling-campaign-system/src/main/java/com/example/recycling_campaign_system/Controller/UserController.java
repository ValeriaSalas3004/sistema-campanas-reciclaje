package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.User;
import com.example.recycling_campaign_system.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Validated @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        if (user.getId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario con el id " + user.getId() + " ya se encuenra registrado");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(user));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByName(@PathVariable Integer id) {
        if (this.service.findByIdUser(id) == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El id no se encuentra registrado");
        }
        return ResponseEntity.ok(this.service.findByIdUser(id));
    }

}
