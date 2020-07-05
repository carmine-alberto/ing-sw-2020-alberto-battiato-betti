package it.polimi.ingsw.model;


import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.Build;
import it.polimi.ingsw.model.actions.Move;
import it.polimi.ingsw.model.phases.Node;
import it.polimi.ingsw.model.phases.TurnPhase;
import it.polimi.ingsw.model.predicates.actionPredicate.CanMovePredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsCellFreePredicate;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.IsDeltaHeightLessThanPredicate;
import it.polimi.ingsw.model.predicates.constructiblePredicates.BlockPredicate;
import it.polimi.ingsw.model.predicates.winConditionsPredicates.WinningMovePredicate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.BiPredicate;

import static it.polimi.ingsw.GameSettings.*;

public class God implements Serializable {

    private String name;
    private transient BiPredicate<Game, GameWorker> winConditionPredicate;

    private transient Action moveStrategy;
    private transient Action buildStrategy;
    private transient Node phasesTree; //Pointing to root, ALWAYS
    private transient Node currentPhaseNode; //Pointing to the current phase

    private transient HashMap<String, BiPredicate> outerPredicatesHashmap;

    private transient BiPredicate<FieldCell, GameWorker> buildPredicates;
    private transient BiPredicate<Player, Constructible> constructiblePredicates;
    private transient BiPredicate<FieldCell, GameWorker> movePredicates;
    private transient BiPredicate<ActionEnum, Player> actionPredicates;

    private God() {
        winConditionPredicate = new WinningMovePredicate();
        buildPredicates = new IsCellFreePredicate();
        constructiblePredicates = new BlockPredicate(3);
        movePredicates = new IsCellFreePredicate().and(new IsDeltaHeightLessThanPredicate(2));
        actionPredicates = new CanMovePredicate();
        buildStrategy = new Build();
        moveStrategy = new Move();
        outerPredicatesHashmap = new HashMap<>();
    }

    /**
     * This function is used to get the next turnPhase form the phaseTree of the selected god.
     *
     * @param currentGame The game you're playing
     * @return Return the following phase (or null if something goes wrong)
     */
    public TurnPhase getNextPhase(Game currentGame) {
        try {
            String nextPhase = currentPhaseNode.getPhase();
            BiPredicate phasePredicate = currentPhaseNode.getPhasePredicate(); // TODO fix

            TurnPhase newPhase = (TurnPhase) Class.forName(TurnPhase.class.getPackageName() + PACKAGE_SEPARATOR + nextPhase)
                    .getConstructors()[FIRST_ELEMENT_INDEX]
                    .newInstance(currentGame, phasePredicate);

            return newPhase;
        } catch (Exception e) {
            e.printStackTrace(); //TODO Handle exception properly
            return null;
        }
    }


    /**
     * Assumption: the only node with more than 1 child is the ChooseActionPhase node
     * if this assumption holds, the method works
     *
     * @param currentGame The game you're playing
     */
    public void setNextPhase(Game currentGame) {
        if (currentPhaseNode.getChildren().size() > ONE) {
            String selectedAction = currentGame.getTurnPlayer().getPlayerState().getSelectedAction().toString();

            currentPhaseNode = currentPhaseNode
                    .getChildren()
                    .stream()
                    .filter(node -> node.getPhase().toUpperCase().startsWith(selectedAction.toUpperCase()))
                    .findFirst()
                    .get();
        }
        else if (currentPhaseNode.getChildren().size() == ONE)
            currentPhaseNode =  currentPhaseNode.getChildren().get(FIRST_ELEMENT_INDEX);
        else { //Leaf Node - we are in the EndPhase

            reset(); //The next phase will be the root of the tree - ugly way to manage a de-facto graph
        }
    }

    /**
     * This function returns the biPredicate, assigned in the outerPredicateHashMap of the god
     * @param predicateClass String key of the hashMap value
     * @return biPredicate assigned to that key
     */
    public BiPredicate getOuterPredicate(String predicateClass){
        return outerPredicatesHashmap.get(predicateClass);
    }

    private void reset() {
        currentPhaseNode = phasesTree;
    }

    public String getName() {
        return name;
    }

    public void setMovePredicates(BiPredicate<FieldCell, GameWorker> movePredicates) {
        this.movePredicates = movePredicates;
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

    public BiPredicate<Game, GameWorker> getWinCondition() {
        return winConditionPredicate;
    }

    /**
     * This function sets the biPredicate to a string (meaning the predicate class) in the outerPredicateHashMap of the god
     * @param predicate key of the hashMap
     * @param biPredicate value of the hashMap
     */
    public void setOuterPredicate(String predicate, BiPredicate biPredicate) {
        this.outerPredicatesHashmap.put(predicate, biPredicate);
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

        /**
         * This function adds a phase to the phaseTree. If it has a specific biPredicate it is assigned,
         * otherwise the "standard" biPredicate of that phase is assigned. In that case the biPredicate has to be null
         *
         * @param phaseName Is the string the predicate has to be assigned to
         * @param phasePredicate The specific biPredicate of the phase, if null ,teh standard biPredicate is used
         *
         * @return the node just added to the phasesTree
         */
        public GodBuilder addPhase(String phaseName, BiPredicate phasePredicate) {
            if (phasePredicate == null)
                    phasePredicate = getPhasePredicate(phaseName);

            Node newNode = new Node(currNode, phaseName, phasePredicate);
            currNode.addChild(newNode);
            currNode = newNode;

            return this;
        }

        private BiPredicate getPhasePredicate(String name) {
            return switch (name) {
                case "ChooseActionPhase" -> tempGod.actionPredicates;
                case "MovePhase" -> tempGod.movePredicates;
                case "BuildPhase" -> tempGod.buildPredicates;
                case "ChooseBlockPhase" -> tempGod.constructiblePredicates;
                default -> (arg1, arg2) -> true;
            };

        }

        /**
         * This function adds the values to the outerHashMap, with the meaning of the key for the string and value for the biPredicate
         *
         * @param turnPhase The name of the turnPhase the outerHashMap has to modify
         * @param biPredicate The biPredicate that has to be added to the turnPhase inserted as first value
         *
         * @return actually useless
         */
        //todo refactor return
        public GodBuilder addOuterHashMap(String turnPhase, BiPredicate biPredicate){
            tempGod.outerPredicatesHashmap.put(turnPhase, biPredicate); //todo gestire le concatenazioni con predicates multipli
            return this;
        }

        /**
         * This function is used to save the node, usually the one where the turnTree divides
         *
         * @return the node saved
         */
        public GodBuilder saveRefNode() {
            refNode = currNode;
            return this;
        }

        /**
         * This function is used to get back to the saved node, the division of the two branches in the turnTree
         *
         * @return the node saved before
         */
        public GodBuilder restoreRefNode() {
            currNode = refNode;
            return this;
        }

        /**
         * This function sets the biPredicate given as winConditionPredicate of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param winConditionPredicate the biPredicate you want to add as winConditionPredicate
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder winConditionPredicate(BiPredicate<Game, GameWorker> winConditionPredicate) {
            this.tempGod.winConditionPredicate = winConditionPredicate;
            return this;
        }

        /**
         * This function sets the biPredicate given as movePredicate of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param movePredicate the biPredicate you want to add as movePredicate
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder movePredicate(BiPredicate<FieldCell, GameWorker> movePredicate){
            this.tempGod.movePredicates = movePredicate;
            return this;
        }

        /**
         * This function sets the biPredicate given as buildPredicate of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param buildPredicate the biPredicate you want to add as buildPredicate
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder buildPredicate(BiPredicate<FieldCell, GameWorker> buildPredicate) {
            this.tempGod.buildPredicates = buildPredicate;
            return this;
        }

        /**
         * This function sets the biPredicate given as constructibleBiPredicate of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param constructibleBiPredicate the biPredicate you want to add as constructibleBiPredicate
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder constructiblePredicate(BiPredicate<Player, Constructible> constructibleBiPredicate){
            this.tempGod.constructiblePredicates =  constructibleBiPredicate;
            return this;
        }

        /**
         * This function sets the biPredicate given as moveStrategy of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param moveStrategy the biPredicate you want to add as moveStrategy
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder moveStrategy(Action moveStrategy) {//todo non è meglio usare getter e setter separati ?
            this.tempGod.moveStrategy = moveStrategy;
            return this;
        }

        /**
         * This function sets the biPredicate given as actionPredicate of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param actionEnumBiPredicate the biPredicate you want to add as actionPredicate
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder actionPredicate(BiPredicate<ActionEnum, Player> actionEnumBiPredicate){
            this.tempGod.actionPredicates = actionEnumBiPredicate;
            return this;
        }

        /**
         * This function sets the biPredicate given as buildStrategy of the god, if you want to have
         * multiple biPredicate, you have to concatenate them externally
         *
         * @param buildStrategy the biPredicate you want to add as buildStrategy
         *
         * @return the godBuilder in the actual state
         */
        public GodBuilder buildStrategy(Action buildStrategy) {
            this.tempGod.buildStrategy = buildStrategy;
            return this;
        }

        /**
         * This function is used to extract the god built with all the settings given,
         * if the phasesTree is empty it will be filled with the basePhasesTree
         *
         * @return the god built
         */
        public God getCompleteGod() {
            if(tempGod.phasesTree.getChildren().size() == EMPTY)
                setBasePhases();
            //We're assuming it's the only one, since every turn starts with WorkerSelection
            tempGod.phasesTree = tempGod.phasesTree.getChildren().get(FIRST_ELEMENT_INDEX);   //Root is null, set the first child as new root.
            tempGod.currentPhaseNode = tempGod.phasesTree;


            God completeGod = tempGod;
            reset();

            return completeGod;
        }

        private void setBasePhases() {

            this
                    .addPhase("ChooseWorkerPhase", (arg1, arg2) -> true)
                    .addPhase("ChooseActionPhase", tempGod.actionPredicates)
                    .addPhase("MovePhase", tempGod.movePredicates)
                    .addPhase("BuildPhase", tempGod.buildPredicates)
                    .addPhase("ChooseBlockPhase", tempGod.constructiblePredicates)
                    .addPhase("EndPhase", (arg1, arg2) -> true);

        }

        /**
         * This function is used to reset the godBuilder, it is already called in getCompleteGod(),
         * but can also be called externally for any needs.
         */
        public void reset() {
            tempGod = new God();
            tempGod.phasesTree = new Node(null, null, null); //ROOT - Modified by the GodBuilder
            refNode = tempGod.phasesTree;
            currNode = tempGod.phasesTree;
        }
    }



}