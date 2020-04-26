package it.polimi.ingsw.model;

import it.polimi.ingsw.model.predicates.movePredicates.MovePredicate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Objects;

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

            visitNoder(doc, builder);

            System.out.println("----------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void visitNoder(Node node, God.GodBuilder god/*, BiPredicate tempPredicate*/) {
        NodeList nList = node.getChildNodes();


        for (Integer i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {         //CHILD NODE HAS NO TEXT (e.g. <and></and>)
                //conjunctionReader(nList.item(i), level);
                System.out.println("<" + nList.item(i).getNodeName() + ">");

                readNode(nList.item(i), god);

                System.out.println("FINE <" + nList.item(i).getNodeName() + ">");
            } else                                                //CHILD NODE HAS TEXT (e.g. <movePredicate>text</movePredicate>)
                System.out.println(nList.item(i).getTextContent());
        }
    }

    private static void readNode(Node node, God.GodBuilder god) {
        String string = "";

        switch (node.getNodeName()) {
            case "name":
                string = readName(node);
                god.name(string);
                string = "";
                break;
            case "movePredicates":
                // MovePredicate movePredicate;
                string = readMovePredicates(node, god, string);
                //attenzione and or e concatenazioni, magari lo faccio mettere in una stringa
                break;
            case "moveStrategy":
                break;
            case "buildPredicates":
                break;
            case "buildStrategy":
                break;
            case "phases":
                buildPhases(node, god);
                break;
            case "winConditions":
                break;
            case "constructiblePredicates":
                break;
            /*default:
                visitNoder(node, level + 1, god);*/
        }

    }

    private static void buildPhases(Node item, God.GodBuilder god) {
        //Ci pensa bat LOL
    }

    private static String readMovePredicates(Node node, God.GodBuilder god, String string) {
        NodeList nList = node.getChildNodes();


        for (Integer i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {
                switch (nList.item(i).getNodeName()) {
                    case "name":
                        string = string.concat(Objects.requireNonNull(readName(nList.item(i))));
                        break;
                    case "args":
                        god.movePredicate(string);
                        string = ("(" + Objects.requireNonNull(readName(nList.item(i))) + ")");
                        break;
                    case "movePredicate":
                        readMovePredicates(nList.item(i), god, string);
                        break;
                    case "and":
                        readAnd(nList.item(i), god, string);
                        break;
                    case "or":
                        readOr(nList.item(i), god, string);
                        break;
                }
            } else
                return string.concat(nList.item(i).getTextContent());
        }
        return string;
    }

    private static void readOr(Node node, God.GodBuilder god, String string) {
    }

    private static void readAnd(Node node, God.GodBuilder god, String string) {

        NodeList nList = node.getChildNodes();


    }

    private static String readName(Node item) {
        int i = 0;
        NodeList nList = item.getChildNodes();

        if (nList.item(i).getNodeValue() != null)
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

