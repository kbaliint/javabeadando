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

    public int count() {
        return positions.size();
    }

    public double calculateFloatingPL(ForexService forexService) {
        double sum = 0;

        for (Position p : positions) {
            double current = forexService.getCurrentPrice(p.getInstrument());

            if (p.getSide().equals("LONG"))
                sum += (current - p.getOpenPrice()) * p.getUnits();
            else
                sum += (p.getOpenPrice() - current) * p.getUnits();
        }

        return sum;
    }

    // ✅ KERESÉS tradeId alapján
    public Position findById(long id) {
        return positions.stream()
                .filter(p -> p.getTradeId() == id)
                .findFirst()
                .orElse(null);
    }

    // ✅ TÖRLÉS (ZÁRÁS)
    public void remove(Position position) {
        positions.remove(position);
    }
}
