package ru.rsreu.searcher;

import ru.rsreu.Maze;

import java.util.LinkedList;
import java.util.List;

public class BFSPathSearcher extends PathSearcher {
    public BFSPathSearcher(Maze maze, Coordinate start, Coordinate end) {
        super(maze, start, end);
    }

    @Override
    public List<Coordinate> search() {
        return searchAnswer(start);
    }

    public LinkedList<Coordinate> searchAnswer(Coordinate start) {
        LinkedList<MazeTreeNode> queue = new LinkedList<>();

        queue.add(new MazeTreeNode(null, start));
        boolean isFinalSituationFound = false;
        while (!queue.isEmpty() && !isFinalSituationFound) {

            MazeTreeNode parentNode = queue.peek().parent();
            Coordinate current = queue.remove().current();

            // текущий родитель
            MazeTreeNode currentTreeNode = new MazeTreeNode(parentNode, current);

            for (Coordinate situation : getPossiblePassages(current)) {

                MazeTreeNode treeNode = new MazeTreeNode(currentTreeNode, situation);
                // если найденного узла с ситуацией не было в очереди
                if (!queue.contains(treeNode)) {
                    // поместить узел в конец очереди
                    queue.add(treeNode);
                    if (situation.equals(end)) {
                        isFinalSituationFound = true;
                        break;
                    }
                }
            }
        }

        MazeTreeNode lastNode;
        // если финальная ситуация не достигнута, то тогда очередь пустая
        if (isFinalSituationFound) {
            lastNode = queue.getLast();
        } else {
            return new LinkedList<>();
        }
        return findPathByFinalSituationParents(lastNode);
    }

    private static LinkedList<Coordinate> findPathByFinalSituationParents(MazeTreeNode situation) {
        LinkedList<Coordinate> answerStack = new LinkedList<>();
        answerStack.addLast(situation.current());
        var currentSituation = new MazeTreeNode(situation);
        while (currentSituation.parent() != null) {
            currentSituation = currentSituation.parent();
            answerStack.addLast(currentSituation.current());
        }
        return answerStack;
    }
}
