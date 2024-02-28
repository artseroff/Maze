package ru.rsreu.searcher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CoordinateTest {

    @ParameterizedTest
    @MethodSource("invalidCoordinates")
    public void CoordinateCreationExceptionTest(int i, int j) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coordinate(i, j));
    }

    public static Arguments[] invalidCoordinates() {
        return new Arguments[] {
            Arguments.of(-1, 0),
            Arguments.of(0, -1),
            Arguments.of(-100, -100)
        };
    }

    @RepeatedTest(100)
    public void getRandom() {
        int n = 100;
        int m = 100;
        Coordinate coordinate = Coordinate.getRandom(n, m);
        Assertions.assertTrue(coordinate.row() >= 0 && coordinate.row() < n);
        Assertions.assertTrue(coordinate.col() >= 0 && coordinate.col() < m);
    }
}
