package battleship.Enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ShipType {
    AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    DESTROYER(2, "Destroyer");

    private static final Map<Integer, ShipType> SHIP_TYPE_MAP;
    private final int size;
    private final String name;

    ShipType(int size, String name) {
        this.size = size;
        this.name = name;
    }

    static {
        Map<Integer, ShipType> map = new ConcurrentHashMap<>();
        for (ShipType instance : ShipType.values()) {
            map.put(instance.ordinal(), instance);
        }
        SHIP_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    public static ShipType get(int id) {
        return SHIP_TYPE_MAP.get(id);
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return name;
    }


}
