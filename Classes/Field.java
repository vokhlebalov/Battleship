package battleship.Classes;

import battleship.Enums.CellStatus;
import battleship.Enums.FieldStatus;
import battleship.Enums.ShipType;
import battleship.Exceptions.ShipLocationException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static battleship.Enums.CellStatus.*;
import static battleship.Enums.FieldStatus.*;


public class Field {
    private static final int FIELD_SIZE = 10;
    private static final Map<String, Integer> COORDINATES_MAP;
    private static final int SHIPS_NUM = 5;

    static {
        Map<String, Integer> coordinatesMap = new HashMap<>();

        for (char i = 'A', k = 0; k < FIELD_SIZE; i++, k++) {
            coordinatesMap.put(i + "", (int)k);
        }

        for (int i = 1; i <= FIELD_SIZE; i++) {
            coordinatesMap.put(i + "", i - 1);
        }

        COORDINATES_MAP = Collections.unmodifiableMap(coordinatesMap);
    }

    private final Cell[][] cells    =   new Cell[FIELD_SIZE][FIELD_SIZE];
    private final List<Ship> ships  =   new ArrayList<>();

    private FieldStatus status = FieldStatus.SETTING_SHIPS;
    private ShipType shipToSet = ShipType.AIRCRAFT_CARRIER;
    private boolean hidden = false;

    private int shipsAlive = SHIPS_NUM;

    public ShipType getShipToSet() {
        return shipToSet;
    }

    public FieldStatus getStatus() {
        return status;
    }

    public Field() {
        initCells();
    }

    @Override
    public String toString() {
        String field = " ";
        for (int i = 1; i <= FIELD_SIZE; field += " " + i++);
        for (int i = 0; i < FIELD_SIZE; i++) {
            field += "\n" + (char)('A' + i);
            for (int j = 0; j < FIELD_SIZE; j++) {
                CellStatus cellStatus = getCell(j, i).getStatus();
                if (cellStatus.equals(SHIP) && hidden) {
                    field += " " + UNKNOWN;
                } else {
                    field += " " + cellStatus;
                }
            }
        }

        return field;
    }

    public void changeVisibility() {
        hidden = !hidden;
    }

    public void setOnReady() {
        status = ALIVE;
    }

    public void takeHit(String coordinates) {
        coordinates = coordinates.trim();

        if (coordinates.length() == 0) {
            throw new IllegalArgumentException("You didn't enter anything!");
        }

        Cell cellToShoot = getCell(coordinates);
        if (MISS.equals(cellToShoot.getStatus())) {
            status = MISSED;
            return;
        }

        if (HIT.equals(cellToShoot.getStatus())) {
            status = SHIP_HIT;
            return;
        }

        Optional<Ship> shipToShoot = ships.stream().filter(ship -> ship.hasCell(cellToShoot)).findAny();
        if (shipToShoot.isPresent()) {
            cellToShoot.setStatus(HIT);
            if (shipToShoot.get().isAlive()) {
                status = SHIP_HIT;
            } else {
                shipsAlive--;
                status = shipsAlive > 0 ? SHIP_SANK : DEAD;
            }
        } else {
            cellToShoot.setStatus(MISS);
            status = MISSED;
        }
    }

    public void addShip(String coordinates) throws ShipLocationException {
        if (!status.equals(FieldStatus.SETTING_SHIPS)) {
            throw new IllegalStateException("All ships are already on the field");
        }

        coordinates = coordinates.trim();

        if (coordinates.length() == 0) {
            throw new IllegalArgumentException("You didn't enter anything!");
        }

        String[] cellsBuffer = coordinates.split(" +");

        if (cellsBuffer.length != 2) {
            throw new IllegalArgumentException("Entered coordinates do not match the format!");
        }

        List<Cell> startEndCells = Stream.of(
                getCell(cellsBuffer[0]),
                getCell(cellsBuffer[1])
            ).sorted().collect(Collectors.toList());

        Cell startCell = startEndCells.get(0);
        Cell endCell = startEndCells.get(1);

        int startX = startCell.getX(), startY = startCell.getY(),
            endX = endCell.getX(), endY = endCell.getY();

        if (startX != endX && startY != endY){
            throw new ShipLocationException("Wrong ship location!");
        }

        int vShipLength = endY - startY + 1;
        int hShipLength = endX - startX + 1;

        int shipLength = Math.max(vShipLength, hShipLength);
        if (shipLength != shipToSet.getSize()) {
            throw new ShipLocationException("Wrong length of the " + shipToSet + "!");
        }

        int xChanger = hShipLength > 1 ? 1 : 0;
        int yChanger = vShipLength > 1 ? 1 : 0;

        Cell[] shipCells = new Cell[shipLength];

        for (int x = startX, y = startY, i = 0;
             x <= endX && y <= endY;
             x += xChanger, y += yChanger, i++) {
            shipCells[i] = getCell(x, y);
        }

        if (Arrays.stream(shipCells)
                        .anyMatch(
                                cell -> cell.getStatus().equals(SHIP) ||
                                        cell.getStatus().equals(SHIP_NEARBY)
                        )) {
            throw new ShipLocationException("You placed it too close to another one.");
        }

        for (Cell shipCell : shipCells) {
            if (shipCell.getX() - 1 >= 0) {
                getCell(shipCell.getX() - 1, shipCell.getY()).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getX() + 1 < FIELD_SIZE) {
                getCell(shipCell.getX() + 1, shipCell.getY()).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getY() + 1 < FIELD_SIZE) {
                getCell(shipCell.getX(), shipCell.getY() + 1).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getY() - 1 >= 0) {
                getCell(shipCell.getX(), shipCell.getY() - 1).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getX() - 1 >= 0 && shipCell.getY() - 1 >= 0) {
                getCell(shipCell.getX() - 1, shipCell.getY() - 1).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getX() + 1 < FIELD_SIZE && shipCell.getY() - 1 >= 0) {
                getCell(shipCell.getX() + 1, shipCell.getY() - 1).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getX() - 1 >= 0 && shipCell.getY() + 1 < FIELD_SIZE) {
                getCell(shipCell.getX() - 1, shipCell.getY() + 1).setStatus(SHIP_NEARBY);
            }
            if (shipCell.getX() + 1 < FIELD_SIZE && shipCell.getY() + 1 < FIELD_SIZE) {
                getCell(shipCell.getX() + 1, shipCell.getY() + 1).setStatus(SHIP_NEARBY);
            }
        }

        ships.add(new Ship(shipToSet, shipCells));

        shipToSet = ShipType.get(ships.size());
        status = shipToSet == null ? FieldStatus.ALIVE : status;
    }

    private void initCells() {

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(j, i, UNTOUCHED);
            }
        }
    }

    private Cell getCell(String coordinates) {
        int x,y;

        try {
            y = COORDINATES_MAP.get(coordinates.charAt(0) + "");
            x = COORDINATES_MAP.get(coordinates.substring(1));

            return cells[y][x];
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("You entered the wrong coordinates!");
        }
    }

    private Cell getCell(int x, int y) {
        return cells[y][x];
    }
}
