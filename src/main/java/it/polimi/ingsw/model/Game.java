package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.GameStartedEvent;
import it.polimi.ingsw.cview.serverView.VirtualBoardView;
import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import it.polimi.ingsw.model.exceptions.AlreadyExistingNameException;
import it.polimi.ingsw.model.exceptions.IllegalFormatException;
import it.polimi.ingsw.model.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.phases.ChooseWorkerPhase;
import it.polimi.ingsw.model.phases.TurnPhase;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

public class Game extends Observable<Event> {
    public Integer NUM_OF_PLAYERS;

    private Player turnPlayer;  //TODO ind turnPlayer to currentPlayerIndex to enforce synchronization
    private Integer currentPlayerIndex;
    private List<Player> players;
    private List<String> godPowers;

    private TurnPhase turnPhase;

    private FieldCell[][] field = new FieldCell[FIELD_SIZE][FIELD_SIZE];

    public static Integer FIELD_SIZE = 5;

    public Game() {
        players = new ArrayList<>();
        readGodPowers();
        NUM_OF_PLAYERS = -1;

        for (Integer i = 0; i < FIELD_SIZE; i++)
            for (Integer j = 0; j < FIELD_SIZE; j++)
                this.field[i][j] = new FieldCell(this, i, j);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setTurnPlayer(Player currentTurnPlayer) {
        this.turnPlayer = currentTurnPlayer;
    }

    public FieldCell getCell(Integer x, Integer y) {
        return field[x][y];
    }

    public void addPlayer(Player player) throws AlreadyExistingNameException {
       for (Player tmp : players)
                if (tmp.getNickname().equals(player.getNickname()))
                    throw new AlreadyExistingNameException("Questo Nickname è stato già utilizzato, sceglierne un altro.");
            players.add(player);

    }

    /**
     * Sets the currentPlayerIndex, used to extract the turnPlayers from the players list
     *
     * @param currentPlayerIndex as passed by view (incremented by one, to improve user experience)
     */
    public void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void initGame() {
        notifyObservers(new GameStartedEvent());
        turnPhase = new ChooseWorkerPhase(this);
        turnPhase.stateInit();
    }


    public void setPhase(TurnPhase nextTurnPhase) {
        this.turnPhase = nextTurnPhase;
    }

    public void removeTurnPlayer() {
        players.remove(turnPlayer);
        currentPlayerIndex--; //Used to handle third to first player case
        if (players.size() == 1) {
            players.get(0).setIsWinner(true);
            endGame();
        }
    }

    public void endGame() {
        Player winner = players.stream()
                .filter(player -> player.getIsWinner())
                .collect(Collectors.toList())
                .get(0);
        //TODO Handle victory
    }

    public void setNextTurnPlayer() {
        this.turnPlayer = players.get(getNextPlayerIndex());
    }

    private Integer getNextPlayerIndex() {
        currentPlayerIndex++;
        return currentPlayerIndex % players.size();
    }


    public Player getTurnPlayer() {
        if (turnPlayer == null)
            turnPlayer = players.get(currentPlayerIndex);
        return turnPlayer;
    }


    public List<String> getGodPowers() {
        return godPowers;
    }

    public void setGodPowers(List<String> godPowers) {
        this.godPowers = godPowers;
    }

    private void readGodPowers() {
        godPowers = new ArrayList<>(List.of("Apollo", "Artemis", "Athena", "Atlas", "Hephaestus", "Demeter", "Minotaur", "Pan"));
        //TODO Read available godPowers from a file
        // ("file:" + System.getProperty("user.dir") + File.separator + "resources" + File.separator + "copertina_santorini_2016.jpg")
       /* try {

            File xmlFile = new File("/home/batjacopo/IdeaProjects/ing-sw-2020-alberto-battiato-betti/pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("staff");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("Staff id : " + eElement.getAttribute("id"));
                    System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    public void notifyObservers(Event e) {
        this.notify(e);
    }

    public void notifyTurnPlayer(Event event) {
        this.notify(observers
                .stream()
                .filter(observer -> ((VirtualBoardView)observer).getVirtualView().equals(turnPlayer.getPlayerView()))
                .collect(Collectors.toList())
                .get(0),
                event);
    }

    FieldCell[][] getField() {
        return this.field;
    }

    public void runPhase(String inputString) throws IllegalFormatException, InvalidSelectionException {
        this.turnPhase.run(inputString);
    }

    public void endPhase() {
        turnPhase.stateEnd();
        turnPhase = turnPhase.getNextPhase();
        turnPhase.stateInit();
    }
}
