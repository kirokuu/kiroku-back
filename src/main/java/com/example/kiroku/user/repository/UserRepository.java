package com.example.kiroku.login.repository;

import com.example.kiroku.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndPassword(String userId, String password);
    Optional<User> findByUserId(String userId);
}
