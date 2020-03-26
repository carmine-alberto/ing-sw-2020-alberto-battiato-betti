package it.polimi.ingsw.model;

import java.util.List;

public class Game {
    Player turnPlayer;

    List<Player> players;

    private FieldCell[][] field = new FieldCell[5][5];

    public List<Player> getPlayers() {
        return players;
    }

    public FieldCell getCell(Integer x, Integer y) {
        return field[x][y];
    }

    public GameWorker getWorkerFromPos(Integer x, Integer y){
        GameWorker worker = null;

        return worker;
    }

    public Boolean isCellAvailable(Integer x, Integer y){
        FieldCell cell = null; // TODO da modificare

        return cell.isFree();
    }
}
