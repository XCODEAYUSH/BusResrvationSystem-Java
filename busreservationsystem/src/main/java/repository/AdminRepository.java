package com.aqm.repository;

import com.aqm.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByEmailAndAdminPassword(String email, String adminPassword);  // ✓ Changed from 'password' to 'adminPassword'
}
