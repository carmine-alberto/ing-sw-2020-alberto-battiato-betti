package it.polimi.ingsw.model;

import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndShiftBack;
import it.polimi.ingsw.model.actions.moveStrategies.MoveAndSwap;
import it.polimi.ingsw.model.predicates.buildAndMovePredicates.BuildAndMovePredicate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.function.BiPredicate;


public class Parser {

    public static void main(String[] args) {
        read();
    }

    private static void read() {
        God.GodBuilder builder = new God.GodBuilder();
        //TODO Add setters for the remaining God attributes in GodBuilder

        try {
            File xmlFile = new File(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "godPowers.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            stripSpace(doc)
            ;
            System.out.println("Root element :" + doc.getDocumentElement());

            //printNode(doc, 0);
            visitNode(doc, builder);

            System.out.println("----------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, Integer level/*, BiPredicate tempPredicate*/) {
        NodeList nList = node.getChildNodes();
        for (Integer i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {         //CHILD NODE HAS NO TEXT (e.g. <and></and>)
                System.out.println(numOfTabs(level) + "<" + nList.item(i).getNodeName() + ">");
                printNode(nList.item(i), level + 1);
                System.out.println(numOfTabs(level) + "FINE<" + nList.item(i).getNodeName() + ">");
            }
            else                                                 //CHILD NODE HAS TEXT (e.g. <movePredicate>text</movePredicate>)
                System.out.println(numOfTabs(level) + "NOT NULL: " + nList.item(i).getTextContent());
        }
    }

    public static boolean isNumeric(String strNum) {
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

    private static void visitNode(Node node, God.GodBuilder god/*, BiPredicate tempPredicate*/) {
        Node child = node.getFirstChild();

        if(child.getNodeName().equals("gods")) { //some "useless" controls just to check if the xml was formatted correctly
            NodeList nList = child.getChildNodes();
            for (int i = 0; i < nList.getLength(); i++)
                if (nList.item(i).getNodeValue() == null)
                    if (nList.item(i).getNodeName().equals("god")) //sto leggendo un god
                        readNode(nList.item(i), god);
        }
    }

    private static void readNode(Node node, God.GodBuilder god) {
        NodeList nList = node.getChildNodes();

        for(int i = 0; i < nList.getLength(); i++) {
            //if (nList.item(i).getNodeValue() == null)
            node = nList.item(i);
            switch (nList.item(i).getNodeName()) {
                case "name":
                    String string = readName(node);
                    god.name(string);
                    break;
                case "movePredicates":
                    BiPredicate<FieldCell, GameWorker> movePredicate = readBuildAndMovePredicates(node, null); //todo elimnare god
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
                    BiPredicate<Game, GameWorker> winConditionPredicate = readWinCondition(node);
                    god.winConditionPredicate(winConditionPredicate);
                    break;
                case "constructiblePredicates":
                    break;
                case "negate":
                    break;
                /*default:
                    visitNoder(node, level + 1, god);*/
            }
        }
    }

    private static BiPredicate<Game, GameWorker> readWinCondition(Node node) { //also sets if is OnOpponents
        Node child = node.getFirstChild();

        if (child.getNodeValue() == null)
            return readWinCondition(child);
        try{
            if (child.getNextSibling() != null )
                if(isNumeric(child.getNextSibling().getFirstChild().getTextContent()))
                 return (BiPredicate<Game, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.winConditionsPredicates." + child.getTextContent())
                         .getConstructors()[0]
                         .newInstance(node.getChildNodes().item(1));
            return (BiPredicate<Game, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.winConditionsPredicates." + child.getTextContent())
                    .getConstructors()[0]
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace(); //TODO Handle exception properly
            return null;
        }
    }

    private static Action readStrategy(Node node) {
        Node child = node.getFirstChild();

        if (child.getNodeValue() != null)
            switch (child.getTextContent()){
                case "MoveAndShiftBack":
                    return new MoveAndShiftBack();
                case "MoveWithSwap":
                    return new MoveAndSwap();
                case "MoveAndSet":
                    /*
                    return new MoveAndSet();
                     */
            }
        return null;
    }

    private static void buildPhases(Node item, God.GodBuilder god) {
        //Ci pensa bat LOL
    }

    private static BiPredicate<FieldCell, GameWorker> readBuildAndMovePredicates(Node node, Integer arg) {
        NodeList nList = node.getChildNodes();
        BiPredicate<FieldCell, GameWorker> buildAndMovePredicate = new BuildAndMovePredicate();

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
                        readBuildAndMovePredicates(nList.item(i), null);
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
                }
            } else{ //gestisco caso base leggo il predicate con la reflection
                try {
                    if(arg == null)
                        buildAndMovePredicate = (BiPredicate<FieldCell, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.buildAndMovePredicates." + nList.item(i).getTextContent())
                                .getConstructors()[0]
                                .newInstance();
                    else buildAndMovePredicate = (BiPredicate<FieldCell, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.buildAndMovePredicates." + nList.item(i).getTextContent())
                            .getConstructors()[0]
                            .newInstance(arg);
                } catch (Exception e) {
                    e.printStackTrace(); //TODO Handle exception properly
                }
            }
        }
        return buildAndMovePredicate;
    }

    private static BiPredicate<FieldCell, GameWorker> readConj(Node node, String type) {
        NodeList nList = node.getChildNodes();
        BiPredicate<FieldCell, GameWorker> firstPredicate = null;
        BiPredicate<FieldCell, GameWorker> secondPredicate = null;

        for(int i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name":
                        if (isNumeric(nList.item(i + 1).getChildNodes().item(0).getTextContent()))/*controllo che effettivamente args ci sia */ {
                            int val = Integer.parseInt(nList.item(i + 1).getChildNodes().item(0).getTextContent());
                            if(i == 0)
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
                            firstPredicate =  readConj(nList.item(i), "and");
                        else
                            secondPredicate = readConj(nList.item(i), "and");
                        break;
                    case "or":
                        if (i == 0)
                            firstPredicate =  readConj(nList.item(i), "or");
                        else
                            secondPredicate = readConj(nList.item(i), "or");
                        break;
                }
            }
        }
        if(firstPredicate != null && secondPredicate != null) {
            if (type.equals("and"))
                return firstPredicate.and(secondPredicate);
            return firstPredicate.or(secondPredicate);
        }
        return null;
    }
/*
    private static BiPredicate<FieldCell, GameWorker> readBuildPredicates(Node node, God.GodBuilder god, Integer arg) {
        NodeList nList = node.getChildNodes();
        BiPredicate<FieldCell, GameWorker> buildPredicate = new BuildAndMovePredicate();

        for (Integer i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name":
                        if (isNumeric(nList.item(i + 1).getChildNodes().item(0).getTextContent())) {
                            int val = Integer.parseInt(nList.item(i + 1).getChildNodes().item(0).getTextContent());
                            buildPredicate = readBuildPredicates(nList.item(i), god, val);
                        } else
                            buildPredicate = readBuildPredicates(nList.item(i), god, null);
                        break;
                 case "buildPredicate":
                        readBuildPredicates(nList.item(i), god, null);
                        break;
                    case "and":
                        buildPredicate = readBuildPredicates(nList.item(i).getChildNodes().item(0), god, null)
                                .and(readBuildPredicates(nList.item(i).getChildNodes().item(1), god, null));
                        break;
                    case "or":
                        buildPredicate = readBuildPredicates(nList.item(i).getChildNodes().item(0), god, null)
                                .or(readBuildPredicates(nList.item(i).getChildNodes().item(1), god, null));
                        break;
                }
            } else{
                try {
                    if(arg == null)
                        buildPredicate = (BiPredicate<FieldCell, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.buildPredicates." + nList.item(i).getTextContent())
                                .getConstructors()[0]
                                .newInstance();
                    else buildPredicate = (BiPredicate<FieldCell, GameWorker>) Class.forName("it.polimi.ingsw.model.predicates.buildPredicates." + nList.item(i).getTextContent())
                            .getConstructors()[0]
                            .newInstance(arg);
                } catch (Exception e) {
                    e.printStackTrace(); //TODO Handle exception properly
                }
            }
        }
        return buildPredicate;
    }
*/
    private static String readName(Node item) {//todo fix, just getfirtchild node
        NodeList nList = item.getChildNodes();

        if (nList.item(0).getNodeValue() != null)
            return nList.item(0).getTextContent();
        return null;
    }

    private static String numOfTabs(Integer level) {
        StringBuilder tabs = new StringBuilder();

        for (Integer i = 0; i < level; i++)
            tabs.append('\t');

        return tabs.toString();

    }

    private static void stripSpace(Node node) {
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
}

