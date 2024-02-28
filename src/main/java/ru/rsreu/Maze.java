package ru.rsreu;

import org.jetbrains.annotations.NotNull;

public record Maze(int rows, int cols, @NotNull Cell[][] grid) {
}
