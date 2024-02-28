package ru.rsreu.searcher;

import java.util.Random;

public record Coordinate(int row, int col) {

    private static final Random RANDOM = new Random();

    public Coordinate {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Координаты клетки лабиринта не должны быть отрицательными");
        }
    }

    public static Coordinate getRandom(int rows, int cols) {
        return new Coordinate(RANDOM.nextInt(rows), RANDOM.nextInt(cols));
    }
}
