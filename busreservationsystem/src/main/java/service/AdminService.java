package com.aqm.service;

import com.aqm.entity.Admin;
import com.aqm.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin registerAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Optional<Admin> login(String email, String password) {
        return adminRepository.findByEmailAndAdminPassword(email, password);

    }
}
