package com.sutej.broadcast.repository;

import com.sutej.broadcast.modals.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken, String> {
//    void findByToken(String token);
    Optional<VerificationToken> findByToken(String token);
}
