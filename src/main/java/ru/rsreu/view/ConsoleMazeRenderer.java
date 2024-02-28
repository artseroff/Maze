package ru.rsreu.view;

import ru.rsreu.Cell;
import ru.rsreu.Maze;
import ru.rsreu.searcher.Coordinate;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ConsoleMazeRenderer {
    public enum Pixel {
        PASSAGE(ConsoleColor.BLACK), WALL(ConsoleColor.GRAY), PATH(ConsoleColor.RED);

        private ConsoleColor color;

        Pixel(ConsoleColor color) {
            this.color = color;
        }

        public ConsoleColor getColor() {
            return color;
        }
    }

    // include 1 wall
    private static final int CELL_SIZE = 4;

    private ConsoleMazeRenderer() {
    }

    public static String getMazeView(@NotNull Maze maze) {
        Pixel[][] pixels = getPixels(maze);
        return render(pixels);
    }

    private static Pixel[][] getPixels(Maze maze) {
        Pixel[][] pixels = new Pixel[maze.rows() * CELL_SIZE + 1][maze.cols() * CELL_SIZE + 1];
        var grid = maze.grid();
        for (int i = 0; i < maze.rows() * CELL_SIZE + 1; i++) {
            for (int j = 0; j < maze.cols() * CELL_SIZE + 1; j++) {
                boolean iMod = i % CELL_SIZE == 0;
                boolean jMod = j % CELL_SIZE == 0;
                if (iMod || jMod) {
                    pixels[i][j] = Pixel.WALL;
                    if (iMod && jMod && i != 0 && j != 0) {
                        updatePixelsFromGridValues(i, j, pixels, grid);
                    }
                } else {
                    pixels[i][j] = Pixel.PASSAGE;

                }
            }
        }

        return pixels;
    }

    public static String getMazeView(@NotNull Maze maze, @NotNull List<Coordinate> path) {
        Pixel[][] pixels = getPixels(maze);
        if (!path.isEmpty()) {
            drawPath(pixels, path);
        }
        return render(pixels);
    }

    private static void drawPath(Pixel[][] pixels, List<Coordinate> path) {
        int index = 0;
        for (Coordinate coordinate : path) {
            int i = coordinate.row() * CELL_SIZE + 1;
            int j = coordinate.col() * CELL_SIZE + 1;
            fillCell(i, j, pixels, CELL_SIZE - 1, CELL_SIZE - 1);
            fillBetweenCells(i, j, pixels, index, path, coordinate);
            index++;
        }
    }

    private static void fillCell(int i, int j, Pixel[][] pixels, int maxI, int maxJ) {
        for (int shiftI = i; shiftI < i + maxI; shiftI++) {
            for (int shiftJ = j; shiftJ < j + maxJ; shiftJ++) {
                pixels[shiftI][shiftJ] = Pixel.PATH;
            }
        }
    }

    private static void fillBetweenCells(
        int i, int j, Pixel[][] pixels, int index, List<Coordinate> path, Coordinate coordinate
    ) {
        if (index >= path.size() - 1) {
            return;
        }
        var neighbour = path.get(index + 1);

        if (coordinate.row() == neighbour.row()) {
            if (neighbour.col() == coordinate.col() + 1) {
                fillCell(i, j + CELL_SIZE - 1, pixels, CELL_SIZE - 1, 1);
            } else if (neighbour.col() == coordinate.col() - 1) {
                fillCell(i, j - 1, pixels, CELL_SIZE - 1, 1);
            }
        } else if (coordinate.col() == neighbour.col()) {
            if (neighbour.row() == coordinate.row() + 1) {
                fillCell(i + CELL_SIZE - 1, j, pixels, 1, CELL_SIZE - 1);
            } else if (neighbour.row() == coordinate.row() - 1) {
                fillCell(i - 1, j, pixels, 1, CELL_SIZE - 1);
            }
        }

    }

    private static void updatePixelsFromGridValues(int i, int j, Pixel[][] pixels, Cell[][] grid) {
        int gridI = (i - CELL_SIZE) / CELL_SIZE;
        int gridJ = (j - CELL_SIZE) / CELL_SIZE;
        Pixel bottomPixel = !grid[gridI][gridJ].isBottomWalled() ? Pixel.PASSAGE : Pixel.WALL;
        Pixel rightPixel = !grid[gridI][gridJ].isRightWalled() ? Pixel.PASSAGE : Pixel.WALL;
        for (int ind = 1; ind < CELL_SIZE; ind++) {
            pixels[i][j - ind] = bottomPixel;
            pixels[i - ind][j] = rightPixel;
        }
    }

    private static String render(Pixel[][] pixels) {
        int n = pixels.length;
        int m = pixels[0].length;
        String resetColor = ConsoleColor.RESET.getBackground();
        StringBuilder view = new StringBuilder(resetColor + "\n\n");
        for (int i = 0; i < n; i += 2) {
            view.append(" ");
            for (int j = 0; j < m; j++) {
                String foreground = pixels[i][j].getColor().getForeground();
                String background = i == n - 1 ? resetColor : pixels[i + 1][j].getColor().getBackground();
                view.append(background).append(foreground).append("▀");
            }
            view.append(resetColor).append("\n");
        }
        return view.toString();
    }
}
