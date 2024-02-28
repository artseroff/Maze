package ru.rsreu.generator;

import ru.rsreu.Maze;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MazeGeneratorTest {
    private static final int N1 = 15;
    private static final int M1 = 40;

    static Arguments[] generators() {

        return new Arguments[] {
            Arguments.of(new DFSMazeGenerator(N1, M1)),
            Arguments.of(new EllerMazeGenerator(N1, M1))
        };
    }

    @ParameterizedTest
    @MethodSource("generators")
    void generateTest(MazeGenerator generator) {
        // тест, что есть хотя бы один проход в соседнюю
        Maze maze = generator.generate();
        var grid = maze.grid();
        for (int i = 0; i < N1; i++) {
            for (int j = 0; j < M1; j++) {
                boolean existsPath = false;
                if (!grid[i][j].isRightWalled()) {
                    existsPath = true;
                }
                if (!grid[i][j].isBottomWalled()) {
                    existsPath = true;
                }
                if (j > 0 && !grid[i][j - 1].isRightWalled()) {
                    existsPath = true;
                }
                if (i > 0 && !grid[i - 1][j].isBottomWalled()) {
                    existsPath = true;
                }
                Assertions.assertTrue(existsPath);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("invalidMazeSizes")
    void generateExceptionTest(int i, int j) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DFSMazeGenerator(i, j));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EllerMazeGenerator(i, j));
    }

    static Arguments[] invalidMazeSizes() {
        return new Arguments[] {
            Arguments.of(-2, -2),
            Arguments.of(2, 0),
            Arguments.of(0, 2),
            Arguments.of(16, 40),
            Arguments.of(15, 41),
            Arguments.of(16, 41)
        };
    }

}
