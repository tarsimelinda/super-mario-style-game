package com.codecool.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RandomService {

    private final Random random = new Random();

    public int betweenInclusive(int min, int max) {
        if (max < min) {
            return min;
        }

        return random.nextInt(max - min + 1) + min;
    }

    public int percent() {
        return random.nextInt(100);
    }

    public <T> T pickOne(List<T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cannot pick from an empty list");
        }

        return items.get(random.nextInt(items.size()));
    }
}