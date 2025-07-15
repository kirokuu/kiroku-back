package com.example.kiroku.user.repository;

import com.example.kiroku.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndPassword(String userId, String password);
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByNicknameAndPassword(String nickname, String password);
    Optional<User> findByUserIdAndNickname(String socialId, String nickname);
}
