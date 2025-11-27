package com.beadando.forexapp.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionService {

    private final List<Position> positions = new ArrayList<>();

    public void add(Position position) {
        positions.add(position);
    }

    public List<Position> getAll() {
        return positions;
    }
}
