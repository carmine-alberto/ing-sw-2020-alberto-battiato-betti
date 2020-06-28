package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.ChangeViewEvent;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.PingEvent;
import it.polimi.ingsw.controller.events.WarningEvent;
import it.polimi.ingsw.cview.ViewState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static it.polimi.ingsw.GameSettings.ONE_SECOND;

public class VirtualView implements Runnable {
        private Socket socket;
        private Controller controller;
        private ObjectInputStream serverInputStream;
        private ObjectOutputStream serverOutputStream;
        private ViewState viewState;
        static Float TIMEOUT = 1F;
        private Boolean isLinkedViewAlive;

        public VirtualView(Socket socket, Controller controller) {
            this.socket = socket;
            this.controller = controller;
            this.viewState = new VirtualLoginViewState(this, socket, viewState);
            this.isLinkedViewAlive = true;
        }

        public void run() {
            try {
                serverInputStream = new ObjectInputStream(socket.getInputStream());
                serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

                //startPingingThread(); TODO toggle

                while (true) {
                    Event receivedEvent = (Event) serverInputStream.readObject();
                    System.out.println(receivedEvent.getClass());
                    controller.handle(receivedEvent, this);
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                //TODO controller.handle(new ClientDisconnectedEvent(), this)
            }
        }

    private void startPingingThread() {
        new Thread(() -> {
            while (true) {
                if (isLinkedViewAlive) {
                    isLinkedViewAlive = false;
                    sendToClient(new PingEvent());
                } else {
                    //TODO controller.handle(new ClientDisconnectedEvent(), this)
                    terminate();
                }
                try {
                    Thread.sleep((long) (TIMEOUT * ONE_SECOND));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendToClient(Event event) {
        try {
            serverOutputStream.writeObject(event);
            serverOutputStream.reset();
        } catch (IOException e) {
            e.printStackTrace(); //TODO Handle closed connection
        }
    }

    public void changeView(ViewState nextState) {
            viewState = nextState;
            sendToClient(new ChangeViewEvent(nextState.toString()));
    }

    public void showMessage(String messageContent) {
            sendToClient(new WarningEvent(messageContent));
    }

    public void terminate() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetTimerFlag() {
        this.isLinkedViewAlive = true;
    }
}

