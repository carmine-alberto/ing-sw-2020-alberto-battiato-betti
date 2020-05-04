package it.polimi.ingsw.model;


import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.phases.Node;
import it.polimi.ingsw.model.phases.TurnPhase;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.BuildAndMovePredicate;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.io.Serializable;
import java.util.function.BiPredicate;

public class God implements Serializable {

    private String name;
    private transient BiPredicate<Game, GameWorker> winConditionPredicate;
    private transient Boolean onOpponents;

    private transient Action moveStrategy;
    private transient Action buildStrategy;
    private transient Node phasesTree; //Pointing to root, ALWAYS
    private transient Node currentPhaseNode; //Pointing to the current phase

    private transient BiPredicate<FieldCell, GameWorker> buildPredicates;
    private transient BiPredicate<Player, Constructible> constructiblePredicates;
    private transient BiPredicate<FieldCell, GameWorker> movePredicates;

    private God() {

        winConditionPredicate = new WinningMovePredicate();
        buildPredicates = new BuildAndMovePredicate();
        constructiblePredicates = new BlockPredicate(3);
        movePredicates = new BuildAndMovePredicate();

        onOpponents = false;
    }


    public TurnPhase getNextPhase(Game currentGame)  {
        try {
            String nextPhase = currentPhaseNode.getPhase();
            BiPredicate phasePredicate = currentPhaseNode.getPhasePredicate();

            TurnPhase newPhase = (TurnPhase) Class.forName("it.polimi.ingsw.model.phases." + nextPhase)
                    .getConstructors()[0]
                    .newInstance(currentGame, phasePredicate);

            return newPhase;
        } catch (Exception e) {
            e.printStackTrace(); //TODO Handle exception properly
        }
        return null;
    }


    /**
     * Assumption: the only node with more than 1 child is the ChooseActionPhase node
     * if this assumption holds, the method works
     * @return
     * @param currentGame
     */
    public void setNextPhase(Game currentGame) {
        if (currentPhaseNode.getChildren().size() > 1) {
            String selectedAction = currentGame.getTurnPlayer().getPlayerState().getSelectedAction().toString();

            currentPhaseNode = currentPhaseNode
                    .getChildren()
                    .stream()
                    .filter(node -> node.getPhase().toUpperCase().startsWith(selectedAction.toUpperCase()))
                    .findFirst()
                    .get();
            return;
        }
        else if (currentPhaseNode.getChildren().size() == 1)
            currentPhaseNode =  currentPhaseNode.getChildren().get(0);
        else //Leaf Node - we are in the EndPhase
            reset(); //The next phase will be the root of the tree - ugly way to manage a de-facto graph

    }

    public void reset() {
        currentPhaseNode = phasesTree;
    }

    public String getName() {
        return name;
    }

    public void assignWinConditionPredicate(Player assignee) { //TODO This method should be called when all players have selected their own god
        if (onOpponents)
            assignee
                    .getOpponents()
                    .forEach(player -> player.setWinConditions(player.getWinConditions().and(winConditionPredicate))); //TODO Is it always "and"?
        else
            assignee.setWinConditions(assignee.getWinConditions().or(winConditionPredicate));
    }

    public Action getMoveStrategy() {
        return this.moveStrategy;
    }

    public Action getBuildStrategy() {
        return this.buildStrategy;
    }

    public BiPredicate<FieldCell, GameWorker> getBuildPredicates() {
        return buildPredicates;
    }

    public BiPredicate<Player, Constructible> getConstructiblePredicates() {
        return constructiblePredicates;
    }

    public BiPredicate<FieldCell, GameWorker> getMovePredicates() {
        return movePredicates;
    }


    /**
     * The tree is built in DFS-like order: first whole branch is read from file,
     * refNode must be saved upon reading "phases" and restored upon exit;
     * then, second branch is read, and so on
     */
    static class GodBuilder {
        private God tempGod;
        private Node refNode;
        private Node currNode;


        GodBuilder() {
            reset();
        }

        public GodBuilder name(String name) {
            tempGod.name = name;
            return this;
        }

        public GodBuilder addPhase(String phaseName, BiPredicate phasePredicate) {
            Node newNode = new Node(currNode, phaseName, phasePredicate);
            currNode.addChild(newNode);
            currNode = newNode;

            return this;
        }

        public GodBuilder saveRefNode() {
            refNode = currNode;
            refNode = tempGod.phasesTree;
            return this;
        }

        public GodBuilder restoreRefNode() {
            currNode = refNode;
            return this;
        }

        public GodBuilder setOnOpponents() {
            this.tempGod.onOpponents = true;
            return this;
        }

        public GodBuilder winConditionPredicate(BiPredicate<Game, GameWorker> winConditionPredicate) {
            this.tempGod.winConditionPredicate = winConditionPredicate;
            return this;
        }

        public GodBuilder movePredicate(BiPredicate<FieldCell, GameWorker> movePredicate){
            this.tempGod.movePredicates = movePredicate;
            return this;
        }

        public GodBuilder buildPredicate(BiPredicate<FieldCell, GameWorker> buildPredicate){
            this.tempGod.movePredicates = buildPredicate;
            return this;
        }

        public GodBuilder constructiblePredicate(BiPredicate<Player, Constructible> constructibleBiPredicate){
            this.tempGod.constructiblePredicates =  constructibleBiPredicate;
            return this;

        }

        public GodBuilder moveStrategy(Action moveStrategy) {//todo non è meglio usare getter e setter separati ?
            this.tempGod.moveStrategy = moveStrategy;
            return this;
        }

        public GodBuilder buildStrategy(Action buildStrategy) {
            this.tempGod.buildStrategy = buildStrategy;
            return this;
        }

        public God getCompleteGod() {
            tempGod.phasesTree = tempGod.phasesTree.getChildren().get(0);   //Root is null, set the first child as new root.
            tempGod.currentPhaseNode = tempGod.phasesTree;                      //We're assuming it's the only one, since every turn starts with WorkerSelection
            God completeGod = tempGod;
            reset();

            return completeGod;
        }

        public void reset() {
            tempGod = new God();
            tempGod.phasesTree = new Node(null, null, null); //ROOT - Modified by the GodBuilder
            refNode = tempGod.phasesTree;
            currNode = tempGod.phasesTree;
        }
    }



}
