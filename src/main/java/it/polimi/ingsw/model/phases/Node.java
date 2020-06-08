package it.polimi.ingsw.model.phases;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Node {
    //Data structure attributes
    private List<Node> children;
    private Node parent;

    //Semantic structures
    private String phase;
    private BiPredicate phasePredicate;



    public Node(Node parent, String phase, BiPredicate phasePredicate) {
        children = new LinkedList<>();
        this.parent = parent;
        this.phase = phase;
        this.phasePredicate = phasePredicate;
    }

    /**
     * this method adds a child to the list of children
     * @param node to add
     */
    public void addChild(Node node) {
        children.add(node);
    }

    public List<Node> getChildren() {
        return children.stream().collect(Collectors.toList());
    }

    public String getPhase() {
        return phase;
    }

    public BiPredicate getPhasePredicate() {
        return phasePredicate;
    }
}
