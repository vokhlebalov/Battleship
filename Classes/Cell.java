package battleship.Classes;

import battleship.Enums.CellStatus;

public class Cell implements Comparable<Cell> {
    private CellStatus status;
    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.status = CellStatus.UNTOUCHED;
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, CellStatus status ) {
        this.status = status;
        this.x = x;
        this.y = y;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellStatus getStatus() {
        return status;
    }


    @Override
    public int compareTo(Cell o) {
        int xDiff = getX() - o.getX();
        int yDiff = getY() - o.getY();

        return xDiff == 0 ? yDiff : xDiff;
    }
}
