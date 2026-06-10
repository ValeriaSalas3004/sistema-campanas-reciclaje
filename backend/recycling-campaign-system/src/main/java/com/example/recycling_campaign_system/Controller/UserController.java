package com.example.recycling_campaign_system.Controller;

import com.example.recycling_campaign_system.Model.DTO.UserLoginDTO;
import com.example.recycling_campaign_system.Model.DTO.UserRequestDTO;
import com.example.recycling_campaign_system.Model.DTO.UserResponseDTO;
import com.example.recycling_campaign_system.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserRequestDTO dto) {

        UserResponseDTO response = service.save(dto);

        if (response == null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {

        List<UserResponseDTO> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {

        UserResponseDTO user = service.findByIdUser(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Integer id, @RequestBody UserRequestDTO dto) {

        UserResponseDTO updatedUser = service.editUser(id, dto);

        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("Email inválido. Formato válido: xxx@xx.xx");
        }

        if (!dto.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
            return ResponseEntity.badRequest().body(
                    "Password inválido. Debe tener 8 caracteres, una mayúscula, un número y un símbolo");
        }

        UserResponseDTO user = service.login(dto);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        boolean deleted = service.delete(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}