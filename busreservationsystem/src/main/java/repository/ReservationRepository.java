package com.aqm.repository;

import com.aqm.entity.Reservation;
import com.aqm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByStatus(String status);
}
