package battleship;

import battleship.Classes.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Game battleship = new Game();
        battleship.run();

        while(battleship.isRunning()) {
            battleship.go(reader.readLine());
        }


    }
}
