package it.polimi.ingsw.model;

public class FieldCell {
    private GameWorker occupyingWorker;
    private Integer posX, posY, height;
    private Boolean hasDome;

    public Integer getHeight(){ return height;};

    public FieldCell(Integer posX, Integer posY) {
        this.occupyingWorker = null;
        this.posX = posX;
        this.posY = posY;
        this.height = 0;
        this.hasDome = false;
    }


    public void incrementHeight(){
        height++;
    };

    public void placeDome(){
        hasDome = true;
    };

    public Boolean isOnPerimeter(){

        if (posX.equals(1) || posX.equals(5) || posY.equals(1) || posY.equals(5))

            return true;

        return false;
    };

    public Boolean isFree() {
        return true; //TODO Implementare metodo
    }

    public GameWorker getWorker() {
        return this.occupyingWorker;
    }
}
