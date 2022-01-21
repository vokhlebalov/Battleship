package battleship.Classes;

import battleship.Enums.CellStatus;
import battleship.Enums.ShipType;

import java.util.List;


public class Ship {
    private final ShipType type;
    private final List<Cell> positionCells;

    public Ship(ShipType type, Cell[] positionCells) {
        this.type = type;
        this.positionCells = List.of(positionCells);
        for (Cell cell :
                positionCells) {
            cell.setStatus(CellStatus.SHIP);
        }
    }

    public boolean hasCell(Cell cell) {
        return positionCells.contains(cell);
    }

    public boolean isAlive() {
        return positionCells.stream().anyMatch(
                cell -> cell.getStatus().equals(CellStatus.SHIP)
        );
    }

    public ShipType getType() {
        return type;
    }
}
