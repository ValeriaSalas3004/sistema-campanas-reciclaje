package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.User;
import com.example.recycling_campaign_system.Repository.UserRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<User> findAll(){
        return this.repository.findAll();
    }

    public User findByIdUser(Integer id){
        Optional<User> optional = this.repository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }



}
