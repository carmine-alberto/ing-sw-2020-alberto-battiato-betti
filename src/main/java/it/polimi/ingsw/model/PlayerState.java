package it.polimi.ingsw.model;

public class PlayerState{
    private transient GameWorker selectedWorker;
    private transient FieldCell selectedCell;
    private transient Constructible selectedConstructible;
    private transient ActionEnum selectedAction;
    private transient Player player;

    public PlayerState(Player player) {
        this.player = player;
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
