package battleship.Classes;

import battleship.Enums.FieldStatus;

public class Player extends Field {
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public FieldStatus Shoot(Player player, String coordinates) {
        player.takeHit(coordinates);

        return player.getStatus();
    }

    public String getName() {
        return name;
    }
}
