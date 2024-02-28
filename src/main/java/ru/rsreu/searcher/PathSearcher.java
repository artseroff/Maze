package ru.rsreu.searcher;

import ru.rsreu.Maze;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class PathSearcher {
    protected Maze maze;
    protected List<Coordinate> path = new ArrayList<>();
    protected Coordinate start;
    protected Coordinate end;

    public PathSearcher(Maze maze, Coordinate start, Coordinate end) {
        if (start.row() >= maze.rows() || start.col() >= maze.cols()) {
            throw new IllegalArgumentException("Начальная клетка выходит за границы лабиринта");
        }
        if (end.row() >= maze.rows() || end.col() >= maze.cols()) {
            throw new IllegalArgumentException("Конечная клетка выходит за границы лабиринта");
        }
        this.maze = maze;
        this.start = start;
        this.end = end;
    }

    public abstract List<Coordinate> search();

    List<Coordinate> getPossiblePassages(Coordinate coordinate) {
        int i = coordinate.row();
        int j = coordinate.col();
        var grid = maze.grid();
        List<Coordinate> list = new LinkedList<>();
        if (j < maze.cols() && !grid[i][j].isRightWalled()) {
            list.add(new Coordinate(i, j + 1));
        }
        if (i < maze.rows() && !grid[i][j].isBottomWalled()) {
            list.add(new Coordinate(i + 1, j));
        }
        if (j > 0 && !grid[i][j - 1].isRightWalled()) {
            list.add(new Coordinate(i, j - 1));
        }
        if (i > 0 && !grid[i - 1][j].isBottomWalled()) {
            list.add(new Coordinate(i - 1, j));
        }
        return list;
    }

}
