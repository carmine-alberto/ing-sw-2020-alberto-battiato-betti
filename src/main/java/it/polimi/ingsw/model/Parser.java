package it.polimi.ingsw.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

            visitNode(doc, 0);

            System.out.println("----------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void visitNode(Node node, Integer level/*, BiPredicate tempPredicate*/) {
        NodeList nList = node.getChildNodes();

        for (Integer i = 0; i < nList.getLength(); i++) {
            if (nList.item(i).getNodeValue() == null) {         //CHILD NODE HAS NO TEXT (e.g. <and></and>)
                System.out.println(numOfTabs(level) + "<" + nList.item(i).getNodeName() + ">");
                visitNode(nList.item(i), level + 1);
            }
            else                                                //CHILD NODE HAS TEXT (e.g. <movePredicate>text</movePredicate>)
                System.out.println(numOfTabs(level) + nList.item(i).getTextContent());
        }
    }
    
    private static String numOfTabs(Integer level) {
        StringBuilder tabs = new StringBuilder();

        for (Integer i = 0; i < level; i++)
            tabs.append('\t');

        return tabs.toString();

    }

    private static void stripSpace(Node node){
        Node child = node.getFirstChild();

        while(child!=null) {
            // save the sibling of the node that will
            // perhaps be removed and set to null
            Node c = child.getNextSibling();
            if ((child.getNodeType()==Node.TEXT_NODE && child.getNodeValue().trim().length()==0)
                    ||
                    ((child.getNodeType()!=Node.TEXT_NODE) && (child.getNodeType()!=Node.ELEMENT_NODE)))
                node.removeChild(child);
            else // process children recursively
                stripSpace(child);
            child = c;
        }
    }

    private static void readNode(NodeList childList) {
        Node child = (Node) childList.item(0);

        switch (child.getNodeName()) {
            case "and":
                readNode(child.getChildNodes());
                System.out.println("And");
                break;
            case "or":
                readNode(child.getChildNodes());
                System.out.println("Or");
                break;
            case "not":
                System.out.println("Not" + child.getNodeName() + "Predicate");
                break;
            default:
                System.out.println(child.getNodeName() + "Predicate");
                break;
        }
    }


}

