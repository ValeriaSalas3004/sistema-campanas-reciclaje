package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.User;
import com.example.recycling_campaign_system.Repository.UserRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User save(User user){
        Optional<User> opt = this.repository.findById(user.getId());
        if (opt.isPresent()){
            return null;
        }
        return this.repository.save(user);
    }
    
}
