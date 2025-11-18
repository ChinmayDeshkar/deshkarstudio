package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUsername(String username);
    void deleteByUsername(String username);
}
