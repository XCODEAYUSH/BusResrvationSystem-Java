package com.aqm.service;

import com.aqm.entity.Route;
import com.aqm.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public Route addRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public List<Route> searchRoutes(String source, String destination) {
        return routeRepository.findBySourceAndDestination(source, destination);
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}
