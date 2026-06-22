package com.example.recycling_campaign_system.service;

import com.example.recycling_campaign_system.model.User;
import com.example.recycling_campaign_system.model.dto.UserLoginDTO;
import com.example.recycling_campaign_system.model.dto.UserRequestDTO;
import com.example.recycling_campaign_system.model.dto.UserResponseDTO;
import com.example.recycling_campaign_system.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserResponseDTO save(UserRequestDTO dto) {

        if (repository.findByEmail(dto.getEmail()) != null) {
            return null;
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        User saved = repository.save(user);

        return toResponseDTO(saved);
    }

    public List<UserResponseDTO> findAll() {

        List<User> users = repository.findAll();
        List<UserResponseDTO> responseList = new ArrayList<>();

        for (User user : users) {
            responseList.add(toResponseDTO(user));
        }

        return responseList;
    }

    public UserResponseDTO findByIdUser(Integer id) {

        Optional<User> optional = repository.findById(id);

        if (optional.isPresent()) {
            return toResponseDTO(optional.get());
        }

        return null;
    }

    public UserResponseDTO editUser(Integer id, UserRequestDTO dto) {

        Optional<User> optional = repository.findById(id);

        if (optional.isEmpty()) {
            return null;
        }

        User user = optional.get();

        if (StringUtils.hasText(dto.getName())) {
            user.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (StringUtils.hasText(dto.getRole())) {
            user.setRole(dto.getRole());
        }

        User updated = repository.save(user);

        return toResponseDTO(updated);
    }

    public UserResponseDTO login(UserLoginDTO dto) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (AuthenticationException e) {
            return null;
        }

        User user = repository.findByEmail(dto.getEmail());

        return user != null ? toResponseDTO(user) : null;
    }

    public boolean delete(Integer id) {

        Optional<User> optional = repository.findById(id);

        if (optional.isEmpty()) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
