package com.aqm.service;

import com.aqm.entity.Bus;
import com.aqm.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BusService {
    @Autowired
    private BusRepository busRepository;

    public Bus addBus(Bus bus) {
        return busRepository.save(bus);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    public Bus updateBus(Bus bus) {
        return busRepository.save(bus);
    }

    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
}
