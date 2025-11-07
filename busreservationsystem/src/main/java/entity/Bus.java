package com.aqm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    @Column(nullable = false, unique = true, length = 20)
    private String busNumber;

    @Column(nullable = false, length = 50)
    private String busType;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(nullable = false)
    private Double farePerSeat;
}
