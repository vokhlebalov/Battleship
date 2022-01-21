package battleship.Exceptions;

/**
 * This exception happens when user is trying to set invalid coordinates for the ship
 * <p>For example:</p>
 * <p>1. Wrong length of the ship</p>
 * <p>2. Location is beyond the field</p>
 * <p>3. Ship placed too close to another one</p>
 */

public class ShipLocationException extends Exception {
    public ShipLocationException(String message) {
        super(message);
    }
}
