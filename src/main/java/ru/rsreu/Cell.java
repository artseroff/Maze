package ru.rsreu;

public class Cell {
    private boolean isRightWalled;
    private boolean isBottomWalled;

    public Cell() {
    }

    public Cell(boolean isRightWalled, boolean isBottomWalled) {
        this.isRightWalled = isRightWalled;
        this.isBottomWalled = isBottomWalled;
    }

    public boolean isRightWalled() {
        return isRightWalled;
    }

    public void setRightWalled(boolean rightWalled) {
        isRightWalled = rightWalled;
    }

    public boolean isBottomWalled() {
        return isBottomWalled;
    }

    public void setBottomWalled(boolean bottomWalled) {
        isBottomWalled = bottomWalled;
    }

}
