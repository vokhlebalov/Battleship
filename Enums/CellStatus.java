package battleship.Enums;

public enum CellStatus {
    UNKNOWN('~'),
    UNTOUCHED('~'),
    SHIP_NEARBY('~'),
    MISS('M'),
    HIT('X'),
    SHIP('O');

    private final char sign;

    CellStatus(char sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return sign + "";
    }
}
