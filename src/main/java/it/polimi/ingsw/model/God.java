package it.polimi.ingsw.model;


import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.phases.Node;
import it.polimi.ingsw.model.phases.TurnPhase;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.util.function.BiPredicate;

public class God {

    private String name;
    private BiPredicate winConditionPredicate;
    private Action moveStrategy;
    private Action buildStrategy;
    private Node phasesTree;
    private Node currentPhaseNode;

    private God() {
        winConditionPredicate = new WinningMovePredicate();
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

    /**
     * The tree is built in DFS-like order: first whole branch is read from file,
     * refNode must be saved upon reading "phases" and restored upon exit;
     * then, second branch is read, and so on
     */
    class GodBuilder {
        private God tempGod;
        private Node refNode;
        private Node currNode;


        public GodBuilder() {
            tempGod = new God();
            tempGod.phasesTree = new Node(null, null, null); //ROOT - Modified by the GodBuilder
            refNode = tempGod.phasesTree;
            currNode = tempGod.phasesTree;
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

        public God getCompleteGod() {
            tempGod.phasesTree = tempGod.phasesTree.getChildren().get(0);   //Root is null, set the first child as new root.
            tempGod.currentPhaseNode = tempGod.phasesTree;                      //We're assuming it's the only one, since every turn starts with WorkerSelection
            God completeGod = tempGod;
            tempGod = new God();

            return completeGod;
        }
    }
}