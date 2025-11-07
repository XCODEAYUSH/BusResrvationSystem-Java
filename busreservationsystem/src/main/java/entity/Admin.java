package com.aqm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")  // ✓ Changed from "admins" to "admin"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "first_name", length = 255)  // ✓ Maps to first_name
    private String firstName;

    @Column(name = "last_name", length = 255)   // ✓ Maps to last_name
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "admin_password", nullable = false, length = 255)  // ✓ Maps to admin_password
    private String adminPassword;

    @Column(name = "mobile_number", length = 10)
    private String mobileNumber;
}
