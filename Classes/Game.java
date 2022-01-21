package battleship.Classes;

import battleship.Enums.FieldStatus;
import battleship.Enums.GameStatus;
import battleship.Exceptions.ShipLocationException;

import java.io.IOException;

import static battleship.Enums.GameStatus.*;

public class Game {
    private GameStatus status = START;
    private final Player firstPlayer = new Player("Player 1");
    private final Player secondPlayer = new Player("Player 2");
    private Player currentPlayer = firstPlayer;

    public void run() {
        status = SETTING_SHIPS;
        System.out.printf("%s, place your ships on the game field\n", currentPlayer.getName());
        printField();
    }

    private Player changeCurrentPlayer() {
        return currentPlayer = currentPlayer.equals(firstPlayer) ? secondPlayer : firstPlayer;
    }

    private void promptEnterKey() {
        System.out.printf(
                "\n%s\n\n" +
                        "Press Enter and pass the move to another player\n" +
                        "...\n",
                currentPlayer
        );
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // TODO change SETTING_SHIPS and PLAY cases
    public void go(String command) {
        clearScreen();

        switch (status) {

            case SETTING_SHIPS:
                if (currentPlayer.getStatus().equals(FieldStatus.SETTING_SHIPS)) {
                    try {
                        currentPlayer.addShip(command);
                        if (currentPlayer.getStatus().equals(FieldStatus.SETTING_SHIPS)) {
                            printField();
                        } else {
                            promptEnterKey();
                            if (currentPlayer.equals(firstPlayer)) {
                                changeCurrentPlayer();
                                System.out.printf("%s, place your ships on the game field\n", currentPlayer.getName());
                                printField();
                            } else {
                                changeCurrentPlayer();
                                printBattlefield();
                                status = PLAY;
                            }
                        }
                    } catch (ShipLocationException e) {
                        System.out.printf("\nError! %s Try again:\n\n", e.getMessage());
                    }
                } else {
                    status = PLAY;
                }

                break;
            case PLAY:
                try {
                    currentPlayer.Shoot(changeCurrentPlayer(), command);
                    switch (currentPlayer.getStatus()) {
                        case MISSED:
                            System.out.println("You missed!\n");
                            promptEnterKey();
                            printBattlefield();
                            currentPlayer.setOnReady();
                            break;

                        case SHIP_HIT:
                            System.out.println("You hit a ship!\n");
                            promptEnterKey();
                            printBattlefield();
                            currentPlayer.setOnReady();
                            break;

                        case SHIP_SANK:
                            System.out.println("You sank a ship!\n");
                            promptEnterKey();
                            printBattlefield();
                            currentPlayer.setOnReady();
                            break;

                        case DEAD:
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            status = END;
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.printf("\nError! %s Try again:\n\n", e.getMessage());
                }

                break;

            case END:
                break;
        }
    }

    private void printField() {
        System.out.printf(
                "\n%s\n\n" +
                        "Enter the coordinates of the %s (%d cells):\n\n",
                currentPlayer,
                currentPlayer.getShipToSet(),
                currentPlayer.getShipToSet().getSize()
        );
    }

    private void printBattlefield() {
        changeCurrentPlayer();
        currentPlayer.changeVisibility();
        System.out.printf("\n%s\n", currentPlayer);
        currentPlayer.changeVisibility();

        changeCurrentPlayer();
        System.out.printf(
                "---------------------\n" +
                        "%s\n\n" +
                        "%s, it's your turn:\n\n",
                currentPlayer,
                currentPlayer.getName()
        );
    }

    public boolean isRunning() {
        return !status.equals(END);
    }
}
