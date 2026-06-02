package com.example.recycling_campaign_system.Repository;

import com.example.recycling_campaign_system.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByName(String name);
    User findByEmail(String email);

    //JPQL
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password =:password")
    User findUserByEmailAndPassword(@Param("email")String email, @Param("password") String password);

}
