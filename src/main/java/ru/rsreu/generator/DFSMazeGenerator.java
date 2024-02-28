package ru.rsreu.generator;

import ru.rsreu.Cell;
import ru.rsreu.Maze;
import ru.rsreu.searcher.Coordinate;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DFSMazeGenerator extends MazeGenerator {

    private final Set<Coordinate> visited;

    public DFSMazeGenerator(int rows, int cols) {
        super(rows, cols);
        visited = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(true, true);
            }
        }
    }

    @Override
    public Maze generate() {
        Coordinate coordinate = Coordinate.getRandom(rows, cols);
        generateRecursive(coordinate);
        return new Maze(rows, cols, grid);
    }

    private void generateRecursive(Coordinate coordinate) {
        visited.add(coordinate);
        List<Coordinate> neighbours = getNeighboursCoordinates(coordinate);
        Collections.shuffle(neighbours);
        for (Coordinate neighbour : neighbours) {
            if (!visited.contains(neighbour)) {
                updateNeighborsPassages(coordinate, neighbour);
                generateRecursive(neighbour);
            }
        }
    }

    private List<Coordinate> getNeighboursCoordinates(Coordinate coordinate) {
        List<Coordinate> neighbours = new LinkedList<>();
        int i = coordinate.row();
        int j = coordinate.col();
        if (i > 0) {
            neighbours.add(new Coordinate(i - 1, j));
        }
        if (i < rows - 1) {
            neighbours.add(new Coordinate(i + 1, j));
        }
        if (j > 0) {
            neighbours.add(new Coordinate(i, j - 1));
        }
        if (j < cols - 1) {
            neighbours.add(new Coordinate(i, j + 1));
        }
        return neighbours;
    }

    private void updateNeighborsPassages(Coordinate coordinate, Coordinate neighbour) {
        int i = coordinate.row();
        int j = coordinate.col();
        if (i == neighbour.row()) {
            if (neighbour.col() == j + 1) {
                grid[i][j].setRightWalled(false);
            } else if (neighbour.col() == j - 1) {
                grid[i][j - 1].setRightWalled(false);
            }
        } else if (j == neighbour.col()) {
            if (neighbour.row() == i + 1) {
                grid[i][j].setBottomWalled(false);
            } else if (neighbour.row() == i - 1) {
                grid[i - 1][j].setBottomWalled(false);
            }
        }
    }
}
