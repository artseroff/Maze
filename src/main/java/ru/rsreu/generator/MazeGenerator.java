package ru.rsreu.generator;

import ru.rsreu.Cell;
import ru.rsreu.Maze;

public abstract class MazeGenerator {
    public static final int MAX_ROWS = 15;
    public static final int MAX_COLS = 40;

    protected final int rows;
    protected final int cols;

    protected final Cell[][] grid;

    public MazeGenerator(int rows, int cols) {
        validateMaze(rows, cols);
        this.rows = rows;
        this.cols = cols;
        grid = new Cell[rows][cols];
    }

    private void validateMaze(int rows, int cols) {
        if (rows < 2 || cols < 2) {
            throw new IllegalArgumentException("Размер лабиринта должен быть не менее 2*2");
        }
        if (rows > MAX_ROWS || cols > MAX_COLS) {
            throw new IllegalArgumentException("Размер лабиринта должен быть не более %dx%d".formatted(
                MAX_ROWS,
                MAX_COLS
            ));
        }
    }

    public abstract Maze generate();

}
