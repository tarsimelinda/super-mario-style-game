package com.codecool.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomServiceTest {

    private RandomService service;

    @BeforeEach
    void setUp() {
        service = new RandomService();
    }

    @Test
    void betweenInclusiveShouldReturnValueWithinRange() {
        int min = 5;
        int max = 10;

        for (int i = 0; i < 100; i++) {
            int result = service.betweenInclusive(min, max);

            assertTrue(result >= min);
            assertTrue(result <= max);
        }
    }

    @Test
    void betweenInclusiveShouldIncludeSingleValueRange() {
        int result = service.betweenInclusive(7, 7);

        assertEquals(7, result);
    }

    @Test
    void betweenInclusiveShouldReturnMinWhenMaxIsLessThanMin() {
        int result = service.betweenInclusive(10, 5);

        assertEquals(10, result);
    }

    @Test
    void percentShouldReturnValueBetweenZeroAndNinetyNine() {
        for (int i = 0; i < 100; i++) {
            int result = service.percent();

            assertTrue(result >= 0);
            assertTrue(result < 100);
        }
    }

    @Test
    void pickOneShouldReturnOneOfTheItems() {
        List<String> items = List.of("red", "blue", "green");

        for (int i = 0; i < 100; i++) {
            String result = service.pickOne(items);

            assertTrue(items.contains(result));
        }
    }

    @Test
    void pickOneShouldReturnTheOnlyItemWhenListHasOneElement() {
        List<String> items = List.of("only");

        String result = service.pickOne(items);

        assertEquals("only", result);
    }

    @Test
    void pickOneShouldThrowExceptionWhenListIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.pickOne(null)
        );

        assertEquals("Cannot pick from an empty list", exception.getMessage());
    }

    @Test
    void pickOneShouldThrowExceptionWhenListIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.pickOne(List.of())
        );

        assertEquals("Cannot pick from an empty list", exception.getMessage());
    }
}