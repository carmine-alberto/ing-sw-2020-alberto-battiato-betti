package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndSet;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndShiftBack;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndSwap;
import it.polimi.ingsw.model.predicates.actionPredicate.CanBuildPredicate;
import it.polimi.ingsw.model.predicates.actionPredicate.CanDisplacePredicate;
import it.polimi.ingsw.model.predicates.actionPredicate.CanMovePredicate;
import it.polimi.ingsw.model.predicates.actionPredicate.CanStopPredicate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;


public class Parser {

    private List<God> godsList = new ArrayList<God>();

    private void read() {
        God.GodBuilder builder = new God.GodBuilder();
        // Add setters for the remaining God attributes in GodBuilder (SHOULD BE DONE)

        try {
            File xmlFile = new File(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "godPowers.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            stripSpace(doc);

            //printNode(doc, 0);
            visitNode(doc, builder);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //used fot debugging purposes only
    private void printNode(Node node, Integer level) {
        NodeList nList = node.getChildNodes();
        for (Integer i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {         //CHILD NODE HAS NO TEXT (e.g. <and></and>)
                System.out.println(numOfTabs(level) + "<" + nList.item(i).getNodeName() + ">");
                printNode(nList.item(i), level + 1);
                System.out.println(numOfTabs(level) + "FINE<" + nList.item(i).getNodeName() + ">");
            } else                                                 //CHILD NODE HAS TEXT (e.g. <movePredicate>text</movePredicate>)
                System.out.println(numOfTabs(level) + "NOT NULL: " + nList.item(i).getTextContent());
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void visitNode(Node node, God.GodBuilder god) {
        Node child = node.getFirstChild();

        if (child.getNodeName().equals("gods")) { //some "useless" controls just to check if the xml was formatted correctly
            NodeList nList = child.getChildNodes();
            for (int i = 0; i < nList.getLength(); i++)
                if (nList.item(i).getNodeValue() == null)
                    if (nList.item(i).getNodeName().equals("god")){ //sto leggendo un god
                        readNode(nList.item(i), god);
                        godsList.add(god.getCompleteGod());
                    }
            // non dovrei fare God.GodBuilder builder = new God.GodBuilder(); per creare un nuovo god ?
        }
    }

    private void readNode(Node node, God.GodBuilder god) {
        NodeList nList = node.getChildNodes();

        for (int i = 0; i < nList.getLength(); i++) {
            //if (nList.item(i).getNodeValue() == null)
            node = nList.item(i);
            switch (node.getNodeName()) {
                case "name":
                    String string = readName(node);
                    god.name(string);
                    break;
                case "movePredicates":
                    BiPredicate<FieldCell, GameWorker> movePredicate = readBuildAndMovePredicates(node, null);
                    god.movePredicate(movePredicate);
                    //attenzione and or e concatenazioni, magari lo faccio mettere in una stringa
                    break;
                case "moveStrategy":
                    Action moveStrategy = readStrategy(node);
                    god.moveStrategy(moveStrategy);
                    break;
                case "buildPredicates":
                    BiPredicate<FieldCell, GameWorker> buildPredicate = readBuildAndMovePredicates(node, null);
                    god.buildPredicate(buildPredicate);
                    break;
                case "buildStrategy":
                    Action buildStrategy = readStrategy(node);
                    god.buildStrategy(buildStrategy);
                    break;
                case "phases":
                    buildPhases(node, god);
                    break;
                case "winConditions":
                    if(node.getFirstChild().getNodeName().equals("outerPredicates"))
                        god.addOuterHashMap("winCondition", readWinCondition(node.getFirstChild(), god, null));
                    else
                    god.winConditionPredicate(readWinCondition(node, god, null));
                    break;
                case "constructiblePredicates":
                    BiPredicate<Player, Constructible> constructiblePredicate = readConstructiblePredicate(node);
                    god.constructiblePredicate(constructiblePredicate);
                    break;
                case "outerPredicates":

            }
        }
    }

    //we assume constructiblePredicates can be fitted with an arg and it is always an int
    private BiPredicate<Player, Constructible> readConstructiblePredicate(Node node) {
        Node child = node.getFirstChild();

       /* if (child.getNodeValue() == null) usless
            return readConstructiblePredicate(child);*/
        try {
            if (child.getNextSibling() != null)
                if (isNumeric(child.getNextSibling().getFirstChild().getTextContent()))
                    return (BiPredicate<Player, Constructible>) Class.forName("it.polimi.ingsw.model.predicates.constructiblePredicates." + child.getTextContent())
                            .getConstructors()[0]
                            .newInstance(Integer.parseInt(child.getNextSibling().getFirstChild().getTextContent()));
            return (BiPredicate<Player, Constructible>) Class.forName("it.polimi.ingsw.model.predicates.constructiblePredicates." + child.getTextContent())
                    .getConstructors()[0]
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace(); //TODO Handle exception properly
            return null;
        }
    }

    private BiPredicate<Game, GameWorker> readWinCondition(Node node, God.GodBuilder god, Integer arg) {
        NodeList nList = node.getChildNodes();
        BiPredicate<Game, GameWorker> winConditionPredicate = null;

        for (int i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name":
                        if (isNumeric(nList.item(i + 1).getChildNodes().item(0).getTextContent()))/*controllo che effettivamente args ci sia */ {
                            int val = Integer.parseInt(nList.item(i + 1).getChildNodes().item(0).getTextContent());
                            winConditionPredicate = readWinCondition(nList.item(i), god, val);
                        } else
                            winConditionPredicate = readWinCondition(nList.item(i), god, null);
                        break;
                /*case "args":      //   QUESTO è CONTENUTO IN NAME(QUANDO C'è C'è SEMPRE ANCHE ARGS)
                       movePredicate = readMovePredicates(nList.item(i), god, )
                 */
                    case "winCondition":
                        winConditionPredicate = readWinCondition(nList.item(i), god, null);
                        break;
                    case "and":
                        winConditionPredicate = readWinConj(nList.item(i), "and", god);
                        break;
                    case "or":
                        winConditionPredicate = readWinConj(nList.item(i), "or", god);
                        break;
                    case "negate":
                        winConditionPredicate = readWinCondition(nList.item(i), god, null).negate();
                        break;
                }
            } else { //gestisco caso base leggo il predicate con la reflection
                try {
                    if (arg == null)
                        winConditionPredicate = (BiPredicate<Game, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.winConditionsPredicates." + nList.item(i).getTextContent())
                                .getConstructors()[0]
                                .newInstance();
                    else
                    { String tmp = nList.item(i).getTextContent();
                        winConditionPredicate = (BiPredicate<Game, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.winConditionsPredicates." + nList.item(i).getTextContent())
                                .getConstructors()[0]
                                .newInstance(arg);}
                } catch (Exception e) {
                    e.printStackTrace(); //TODO Handle exception properly
                }
            }
        }
        return winConditionPredicate;
    }

    private BiPredicate<FieldCell, GameWorker> readBuildAndMovePredicates(Node node, Integer arg) {
        NodeList nList = node.getChildNodes();
        BiPredicate<FieldCell, GameWorker> buildAndMovePredicate = null;

        for (int i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name":
                        if (isNumeric(nList.item(i + 1).getChildNodes().item(0).getTextContent()))/*controllo che effettivamente args ci sia */ {
                            int val = Integer.parseInt(nList.item(i + 1).getChildNodes().item(0).getTextContent());
                            buildAndMovePredicate = readBuildAndMovePredicates(nList.item(i), val);
                        } else
                            buildAndMovePredicate = readBuildAndMovePredicates(nList.item(i), null);
                        break;
                /*case "args":      //   QUESTO è CONTENUTO IN NAME(QUANDO C'è C'è SEMPRE ANCHE ARGS)
                       movePredicate = readMovePredicates(nList.item(i), god, )
                 */
                    case "movePredicate":
                    case "buildPredicate":
                        buildAndMovePredicate = readBuildAndMovePredicates(nList.item(i), null);
                        break;
                    case "and":
                        /*buildAndMovePredicate = readBuildAndMovePredicates(nList.item(i),null)
                                .and(readBuildAndMovePredicates(nList.item(i), -1));*/
                        buildAndMovePredicate = readConj(nList.item(i), "and");
                        break;
                    case "or":
                        /*buildAndMovePredicate = readBuildAndMovePredicates(nList.item(i).getChildNodes().item(0), null)
                                .or(readBuildAndMovePredicates(nList.item(i).getChildNodes().item(1), null));*/
                        buildAndMovePredicate = readConj(nList.item(i), "or");
                        break;
                    case "negate":
                        buildAndMovePredicate = readBuildAndMovePredicates(nList.item(i), null).negate();
                        break;
                }
            } else { //gestisco caso base leggo il predicate con la reflection
                try {
                    if (arg == null)
                        buildAndMovePredicate = (BiPredicate<FieldCell, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.buildAndMovePredicates." + nList.item(i).getTextContent())
                                .getConstructors()[0]
                                .newInstance();
                    else
                        buildAndMovePredicate = (BiPredicate<FieldCell, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.buildAndMovePredicates." + nList.item(i).getTextContent())
                                .getConstructors()[0]
                                .newInstance(arg);
                } catch (Exception e) {
                    e.printStackTrace(); //TODO Handle exception properly
                }
            }
        }
        return buildAndMovePredicate;
    }

    private BiPredicate<Game, GameWorker> readWinConj(Node node, String type, God.GodBuilder god) {
        NodeList nList = node.getChildNodes(); //in case we want other predicates to handle conjunctions we must add it to the switch case
        BiPredicate<Game, GameWorker> firstPredicate = null;
        BiPredicate<Game, GameWorker> secondPredicate = null;

        for (int i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name":
                        break;
                    case "winCondition":
                        //partly redundant
                        if (i == 0)
                            firstPredicate = readWinCondition(nList.item(i), god, null);
                        else
                            secondPredicate = readWinCondition(nList.item(i), god, null);
                        break;
                    case "and":
                        if (i == 0)
                            firstPredicate =  readWinConj(nList.item(i), "and", god);
                        else
                            secondPredicate = readWinConj(nList.item(i), "and", god);
                        break;
                    case "or":
                        if (i == 0)
                            firstPredicate =  readWinConj(nList.item(i), "or", god);
                        else
                            secondPredicate = readWinConj(nList.item(i), "or", god);
                        break;
                }
            }
        }
        if (firstPredicate != null && secondPredicate != null) {
            if (type.equals("and"))
                return firstPredicate.and(secondPredicate);
            return firstPredicate.or(secondPredicate);
        }
        return null;

    }

    private Action readStrategy(Node node) {
        Node child = node.getFirstChild();

        //if (child.getNodeValue() != null)
        switch (child.getTextContent()) {  //TODO Can be replaced using reflection
            case "MoveAndShiftBack":
                return new MoveAndShiftBack();
            case "MoveWithSwap":
                return new MoveAndSwap();
            case "MoveAndSet": //note: move and set can only be provided with a move or build predicate
                //if (child.getNextSibling() != null && child.getNextSibling().getNodeValue() != null)
                    if (child.getNextSibling().getNodeName().equals("arg")) {
                        BiPredicate<FieldCell, GameWorker> predicate;
                        predicate = readBuildAndMovePredicates(child.getNextSibling(), null);
                        return new MoveAndSet(predicate);
                    }
        }
        return null;
    }

    private BiPredicate<FieldCell, GameWorker> readConj(Node node, String type) {
        NodeList nList = node.getChildNodes(); //in case we want other predicates to handle conjunctions we must add it to the switch case
        BiPredicate<FieldCell, GameWorker> firstPredicate = null;
        BiPredicate<FieldCell, GameWorker> secondPredicate = null;

        for (int i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name": // i think it is useless but still needed for reliability
                        if (isNumeric(nList.item(i + 1).getChildNodes().item(0).getTextContent())) {
                            int val = Integer.parseInt(nList.item(i + 1).getChildNodes().item(0).getTextContent());
                            if (i == 0)
                                firstPredicate = readBuildAndMovePredicates(nList.item(i), val);
                            else
                                secondPredicate = readBuildAndMovePredicates(nList.item(i), val);
                        } else {
                            if (i == 0)
                                firstPredicate = readBuildAndMovePredicates(nList.item(i), null);
                            else
                                secondPredicate = readBuildAndMovePredicates(nList.item(i), null);
                        }
                        break;
                    /*case "args":      //   QUESTO è CONTENUTO IN NAME(QUANDO C'è C'è SEMPRE ANCHE ARGS)
                           movePredicate = readMovePredicates(nList.item(i), god, )
                     */
                    case "movePredicate":
                    case "buildPredicate":
                        if (i == 0)
                            firstPredicate = readBuildAndMovePredicates(nList.item(i), null);
                        else
                            secondPredicate = readBuildAndMovePredicates(nList.item(i), null);
                        break;
                    case "and":
                        if (i == 0)
                            firstPredicate = readConj(nList.item(i), "and");
                        else
                            secondPredicate = readConj(nList.item(i), "and");
                        break;
                    case "or":
                        if (i == 0)
                                firstPredicate = readConj(nList.item(i), "or");
                        else
                            secondPredicate = readConj(nList.item(i), "or");
                        break;
                    case "negate":
                        if (i == 0)
                        firstPredicate = readBuildAndMovePredicates(nList.item(i), null).negate();
                    else
                        secondPredicate = readBuildAndMovePredicates(nList.item(i), null).negate();
                        break;
                }
            }
        }
        if (firstPredicate != null && secondPredicate != null) {
            if (type.equals("and"))
                return firstPredicate.and(secondPredicate);
            return firstPredicate.or(secondPredicate);
        }
        return null;
    }

    private String readName(Node item) {
        Node node = item.getFirstChild();

        if (node.getNodeValue() != null)
            return node.getTextContent();
        return null;
    }

    private void buildPhases(Node item, God.GodBuilder god) {
        NodeList nList = item.getChildNodes();

        for (Integer i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeValue() == null) {
                switch (node.getNodeName()) {
                    case "phases":   //new branch
                        god.saveRefNode();
                        buildPhases(node, god);
                        //god.addPhase("EndPhase", (arg1, arg2) -> true); we read it from the file
                        god.restoreRefNode();
                        break;
                    case "name":   //phase introduces a new predicate
                        readPredicate(node, god);
                        i = nList.getLength();  // We arleady read the siblings above
                        break;
                    default:    //<phase>
                        buildPhases(node, god);   //normal phase
                        break;
                }
            } else {//reading the text (di sopra o di sotto?)
                String phaseName = node.getTextContent();
                BiPredicate phasePredicate = null;
                if (phaseName.equals("ChooseActionPhase"))
                    phasePredicate = getActionPredicate(item.getNextSibling());
                god.addPhase(node.getTextContent(), phasePredicate);
            }
        }
    }

   private BiPredicate getActionPredicate(Node item){
       Node node = item.getFirstChild();
       String firstPhase;
       String secondPhase = "End";

        if (item.getNextSibling() != null)
            secondPhase = readName(item.getNextSibling().getFirstChild());

        if (node.getFirstChild().getNodeValue() == null)    //when the phase introduces a new predicate
            node = node.getFirstChild();

        firstPhase = readName(node);

        return StringToPredicate(firstPhase).or(StringToPredicate(secondPhase));

    }

    private BiPredicate StringToPredicate(String phaseName){
        return switch (phaseName){
            case ("MovePhase") -> new CanMovePredicate();
            case ("BuildPhase") -> new CanBuildPredicate();
            case ("DisplacePhase") -> new CanDisplacePredicate();
            default -> new CanStopPredicate();
        };
    }

    private void readPredicate(Node item, God.GodBuilder god) {
        String name = readName(item);
        String predicateName = item.getNextSibling().getNodeName();
        BiPredicate predicate;
        if (predicateName.startsWith("constructible"))
            predicate = readConstructiblePredicate(item.getNextSibling());
        else   //build or move predicate //do we need adding action and winconditionPredicate?
            predicate = readBuildAndMovePredicates(item.getNextSibling(), null);

        god.addPhase(name, predicate);
    }

    private String numOfTabs(Integer level) {
        StringBuilder tabs = new StringBuilder();

        for (Integer i = 0; i < level; i++)
            tabs.append('\t');

        return tabs.toString();

    }

    private void stripSpace(Node node) {
        Node child = node.getFirstChild();

        while (child != null) {
            // save the sibling of the node that will
            // perhaps be removed and set to null
            Node c = child.getNextSibling();
            if ((child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().length() == 0)
                    ||
                    ((child.getNodeType() != Node.TEXT_NODE) && (child.getNodeType() != Node.ELEMENT_NODE)))
                node.removeChild(child);
            else // process children recursively
                stripSpace(child);
            child = c;
        }
    }

    public List<God> getGodsList() {
        read();
        return this.godsList;
    }
}