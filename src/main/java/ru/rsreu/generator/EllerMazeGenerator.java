package ru.rsreu.generator;

import ru.rsreu.Cell;
import ru.rsreu.Maze;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EllerMazeGenerator extends MazeGenerator {

    private static final Random RANDOM = new Random();

    private final List<Set<Cell>> sets;

    public EllerMazeGenerator(int rows, int cols) {
        super(rows, cols);
        sets = new LinkedList<>();
        initSetsWithCellsFromFirstRow(grid[0]);
    }

    @Override
    public Maze generate() {

        for (int i = 0; i < rows; i++) {

            initIRow(i);

            for (int j = 0; j < cols; j++) {
                final Cell currentCell = grid[i][j];
                Set<Cell> foundSet = findSetByCell(currentCell);

                // right
                if (j != cols - 1) {
                    if (foundSet.contains(grid[i][j + 1])) {
                        currentCell.setRightWalled(true);
                    } else {
                        if (RANDOM.nextBoolean()) {
                            currentCell.setRightWalled(true);
                        } else {
                            Set<Cell> foundSetRightCell = findSetByCell(grid[i][j + 1]);
                            foundSet.addAll(foundSetRightCell);
                            sets.remove(foundSetRightCell);
                        }
                    }
                } else {
                    currentCell.setRightWalled(true);
                }
                // down
                if (RANDOM.nextBoolean()) {
                    if (isAnyCellWithoutWalledBottomExceptCellInSet(currentCell, foundSet, grid[i])) {
                        currentCell.setBottomWalled(true);
                    }
                }
            }

        }

        for (int j = 0; j < cols; j++) {
            final Cell currentCell = grid[rows - 1][j];
            currentCell.setBottomWalled(true);
            if (currentCell.isRightWalled() && j < cols - 1) {
                Set<Cell> foundSet = findSetByCell(currentCell);
                if (!foundSet.contains(grid[rows - 1][j + 1])) {
                    Set<Cell> foundSetRightCell = findSetByCell(grid[rows - 1][j + 1]);
                    foundSet.addAll(foundSetRightCell);
                    sets.remove(foundSetRightCell);
                    currentCell.setRightWalled(false);
                }
            }
        }

        return new Maze(rows, cols, grid);
    }

    private boolean isAnyCellWithoutWalledBottomExceptCellInSet(Cell currentCell, Set<Cell> set, Cell[] row) {
        int countCellsWithoutBottom = 0;
        for (Cell elCell : row) {
            if (set.contains(elCell) && !elCell.isBottomWalled()) {
                countCellsWithoutBottom++;
            }
        }

        return (countCellsWithoutBottom > (!currentCell.isBottomWalled() ? 1 : 0));
    }

    private void initIRow(int i) {
        if (i == 0) {
            return;
        }
        for (int j = 0; j < grid[i - 1].length; j++) {
            grid[i][j] = new Cell();
            if (grid[i - 1][j].isBottomWalled()) {
                sets.add(createMazeSetFromOneCell(grid[i][j]));
            } else {
                var set = findSetByCell(grid[i - 1][j]);
                set.add(grid[i][j]);
            }
        }
    }

    private List<Set<Cell>> initSetsWithCellsFromFirstRow(Cell[] row) {

        for (int j = 0; j < row.length; j++) {
            row[j] = new Cell();
            sets.add(createMazeSetFromOneCell(row[j]));
        }
        return sets;
    }

    private Set<Cell> createMazeSetFromOneCell(Cell cell) {
        return new HashSet<>() {{
            add(cell);
        }};
    }

    private Set<Cell> findSetByCell(Cell cell) {

        for (var set : sets) {
            if (set.contains(cell)) {
                return set;
            }
        }
        return null;
    }
}
