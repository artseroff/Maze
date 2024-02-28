package ru.rsreu.searcher;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public record MazeTreeNode(MazeTreeNode parent, @NotNull Coordinate current) {
    public MazeTreeNode(MazeTreeNode situation) {
        this(situation.parent, situation.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, parent);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MazeTreeNode)) {
            return false;
        }
        MazeTreeNode other = (MazeTreeNode) obj;

        return Objects.equals(current, other.current);
        // закоменчено, потому что долго сравнивает по всем родителям
        // && Objects.equals(parent, other.parent);
    }
}
