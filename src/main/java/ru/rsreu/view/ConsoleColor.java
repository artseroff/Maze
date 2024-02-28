package ru.rsreu.view;

public enum ConsoleColor {
    RESET("\u001B[0m"),
    BLACK("\u001B[40m", "\u001B[30m"),
    RED("\u001B[41m", "\u001B[31m"),
    GRAY("\u001B[47m", "\u001B[37m");

    private final String background;
    private final String foreground;

    ConsoleColor(String background, String foreground) {
        this.background = background;
        this.foreground = foreground;
    }

    ConsoleColor(String color) {
        this.background = color;
        this.foreground = color;
    }

    public String getBackground() {
        return background;
    }

    public String getForeground() {
        return foreground;
    }
}
