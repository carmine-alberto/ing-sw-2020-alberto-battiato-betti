package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;

import java.util.List;

public class Game {
    Player turnPlayer;

    List<Player> players;

    private FieldCell[][] field = new FieldCell[5][5];

    public List<Player> getPlayers() {
        return players;
    }

    public void setTurnPlayer(Player currentTurnPlayer){
        this.turnPlayer = currentTurnPlayer;
    }

    public FieldCell getCell(Integer x, Integer y) {
        return field[x][y];
    }

    public void addPlayer(Player player){
        try {
            for(Player tmp : players)
                if(tmp.getNickname().equals(player.getNickname()))
                    throw new AlreadyExistingNameException("Questo Nickname è stato già utilizzato, sceglierne un altro.");
                else players.add(player);
        } catch (AlreadyExistingNameException e){
            //TODO mostrare wiew con messaggio d'errore
        }
    }

}
