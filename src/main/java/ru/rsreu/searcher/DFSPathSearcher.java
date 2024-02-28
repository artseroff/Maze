package ru.rsreu.searcher;

import ru.rsreu.Maze;
import java.util.List;

public class DFSPathSearcher extends PathSearcher {

    public DFSPathSearcher(Maze maze, Coordinate start, Coordinate end) {
        super(maze, start, end);
    }

    @Override
    public List<Coordinate> search() {
        dfs(start);
        return path;
    }

    private boolean dfs(Coordinate coordinate) {
        if (coordinate.equals(end)) {
            path.add(coordinate);
            return true;
        }
        path.add(coordinate);
        for (Coordinate neighbour : getPossiblePassages(coordinate)) {
            if (!path.contains(neighbour)) {
                if (dfs(neighbour)) {
                    return true;
                }
            }
        }
        path.removeLast();
        return false;
    }
}
