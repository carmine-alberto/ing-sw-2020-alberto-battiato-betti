package it.polimi.ingsw.model;

import java.util.List;

public class Game {
    Player turnPlayer;
    List<Player> players;

    private static FieldCell[][] field = new FieldCell[5][5];

    public static FieldCell getCell(String args) {
        return field[1][1]; //TODO Settare args in maniera appropriata
    }
}
