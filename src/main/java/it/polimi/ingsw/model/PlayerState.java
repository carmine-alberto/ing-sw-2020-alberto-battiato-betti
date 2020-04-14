package it.polimi.ingsw.model;

import it.polimi.ingsw.cview.serverView.VirtualView;

public class PlayerState extends Player {
    private transient GameWorker selectedWorker;
    private transient FieldCell selectedCell;
    private transient Constructible selectedConstructible;
    private transient ActionEnum selectedAction;

    public PlayerState(String nickname, VirtualView playerView) {
        super(nickname, playerView);
    }

    public GameWorker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(GameWorker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }


    public FieldCell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(FieldCell selectedCell) {
        this.selectedCell = selectedCell;
    }


    public Constructible getSelectedConstructible() {
        return selectedConstructible;
    }

    public void setSelectedConstructible(Constructible selectedConstructible) {
        this.selectedConstructible = selectedConstructible;
    }


    public ActionEnum getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(ActionEnum selectedAction) {
        this.selectedAction = selectedAction;
    }
}
