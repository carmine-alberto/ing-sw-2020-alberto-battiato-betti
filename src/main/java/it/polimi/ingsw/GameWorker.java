package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class GameWorker {
    public List<FieldCell> getAvailableCells() {
        List<FieldCell> freeCells = new ArrayList<>();
        //Controllo le celle adiacenti: se delta_height_up <= 1 AND !cell.hasDome() AND cell.isFree(), la aggiungo alla lista

        return freeCells;
    }
}
