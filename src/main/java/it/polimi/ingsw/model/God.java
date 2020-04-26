package it.polimi.ingsw.model;


import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndShiftBack;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndSwap;
import it.polimi.ingsw.model.phases.Node;
import it.polimi.ingsw.model.phases.TurnPhase;
import it.polimi.ingsw.model.predicates.actionPredicates.ActionPredicate;
import it.polimi.ingsw.model.predicates.buildPredicates.BuildPredicate;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import it.polimi.ingsw.model.predicates.movePredicates.MovePredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.io.Serializable;
import java.util.function.BiPredicate;

public class God implements Serializable {

    private String name;
    private transient BiPredicate<Game, GameWorker> winConditionPredicate;
    private transient Boolean onOpponents;

    private Action moveStrategy;
    private Action buildStrategy;
    private Node phasesTree; //Pointing to root, ALWAYS
    private Node currentPhaseNode; //Pointing to the current phase

    private transient BiPredicate<FieldCell, GameWorker> buildPredicates;
    private transient BiPredicate<ActionEnum, Player> actionPredicate;
    private transient BiPredicate<Player, Constructible> constructiblePredicates;
    private transient BiPredicate<FieldCell, GameWorker> movePredicates;

    private God() {

        winConditionPredicate = new WinningMovePredicate();
        buildPredicates = new BuildPredicate();
        actionPredicate = new ActionPredicate(true, true, false);
        constructiblePredicates = new BlockPredicate(3);
        movePredicates = new MovePredicate();

        onOpponents = false;
    }

    public TurnPhase nextPhase(Game currentGame, String userSelection) {
        try {
            currentPhaseNode = setNextNode(currentGame);
            String nextPhase = currentPhaseNode.getPhase();
            BiPredicate phasePredicate = phasesTree.getPhasePredicate();
            TurnPhase newPhase = (TurnPhase) Class.forName("it.polimi.ingsw.model.phases" + nextPhase)
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
    private Node setNextNode(Game currentGame) {
        if (currentPhaseNode.getChildren().size() > 1) {
            String selectedPhase = currentGame.getTurnPlayer().getPlayerState().getSelectedAction().toString();
            return currentPhaseNode
                    .getChildren()
                    .stream()
                    .filter(node -> node.getPhase().toUpperCase().contains(selectedPhase.toUpperCase()))
                    .findFirst()
                    .get();
        }
        return currentPhaseNode.getChildren().get(0);
    }

    public void reset() {
        currentPhaseNode = phasesTree;
    }

    public void addBuildPredicates(Player assignee){
    }

    public void assignWinConditionPredicate(Player assignee) { //TODO This method should be called when all players have selected their own god
        if (onOpponents)
            assignee
                    .getOpponents()
                    .forEach(player -> player.setWinConditions(player.getWinConditions().and(winConditionPredicate))); //TODO Is it always "and"?
        else
            assignee.setWinConditions(assignee.getWinConditions().and(winConditionPredicate));
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

        public GodBuilder movePredicate(String string){
            //todo farlo
            return null;
        }

        public GodBuilder moveStrategy(String moveStrategy) {
            if (moveStrategy.equals("moveAndShiftBack")) {
                this.tempGod.moveStrategy = new MoveAndShiftBack();
                return this;
            }
            this.tempGod.moveStrategy = new MoveAndSwap();
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
