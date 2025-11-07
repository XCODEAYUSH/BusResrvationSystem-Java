package com.aqm.service;

import com.aqm.entity.Bus;
import com.aqm.entity.Reservation;
import com.aqm.entity.User;
import com.aqm.repository.BusRepository;
import com.aqm.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BusRepository busRepository;

    @Transactional
    public Reservation createReservation(User user, Bus bus, LocalDate travelDate, Integer numberOfSeats) {
        if (bus.getAvailableSeats() < numberOfSeats) {
            throw new RuntimeException("Not enough seats available");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBus(bus);
        reservation.setReservationDate(LocalDate.now());
        reservation.setTravelDate(travelDate);
        reservation.setNumberOfSeats(numberOfSeats);
        reservation.setTotalFare(bus.getFarePerSeat() * numberOfSeats);
        reservation.setStatus("CONFIRMED");

        bus.setAvailableSeats(bus.getAvailableSeats() - numberOfSeats);
        busRepository.save(bus);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getUserReservations(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> optReservation = reservationRepository.findById(reservationId);
        if (optReservation.isPresent()) {
            Reservation reservation = optReservation.get();
            reservation.setStatus("CANCELLED");

            Bus bus = reservation.getBus();
            bus.setAvailableSeats(bus.getAvailableSeats() + reservation.getNumberOfSeats());
            busRepository.save(bus);

            reservationRepository.save(reservation);
        }
    }
}
