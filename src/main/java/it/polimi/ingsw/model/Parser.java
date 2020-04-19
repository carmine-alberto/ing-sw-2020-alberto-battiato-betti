package it.polimi.ingsw.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Parser {

    public static void main(String[] args) {
        read();
    }

    private static void read() {
        try {

            File xmlFile = new File(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "godpowers.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("god");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                Element god = (Element) nNode;
                System.out.println("\ncurrent element :" + nNode.getNodeName());

                //if (nNode.getNodeType() == Node.ELEMENT_NODE)
                NodeList nodes = god.getChildNodes();


                Node node = nodes.item(0).getNextSibling();
                for(int i = 0; i < nodes.getLength(); i = i + 2){
                /*    node = nodes.item(i + 1).getNextSibling();
                    if(node.getNodeName() != "#text")
                        System.out.println(node.getNodeName());
                  */  node = nodes.item(i).getNextSibling();
                    if(node != null) {
                        System.out.println(node.getNodeName() + ":" + node.getTextContent());
                        if (node.getFirstChild() != null && !node.getFirstChild().getNodeValue().equals(node.getTextContent()))
                            System.out.println(node.getFirstChild().getNodeValue());
                    }
                }
                System.out.println(";\n");
/*
                Node node1 =nodes.item(0).getNextSibling();
                if(node1 != null) {
                    System.out.println(node1.getNodeName() + ":" + node1.getTextContent() + "\n");
                    Node node2 = nodes.item(2).getNextSibling();
                    if(node2 != null){
                        System.out.println(node2.getNodeName() + ":" + node2.getTextContent() + "\n");
                        Node node3 = nodes.item(4).getNextSibling();
                        if(node3 != null)
                            System.out.println(node1.getNodeName() + ":" + node1.getTextContent());
                    }
                }else System.out.println(";\n");*/
            }
        } catch (Exception e) {
            e.printStackTrace();
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

