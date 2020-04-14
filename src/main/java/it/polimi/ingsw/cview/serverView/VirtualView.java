package it.polimi.ingsw.cview.serverView;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.ChangeViewEvent;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.controller.events.WarningEvent;
import it.polimi.ingsw.cview.View;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualView implements Runnable {
        private Player viewOwner;
        private Socket socket;
        private Controller controller;
        private ObjectInputStream serverInputStream;
        private ObjectOutputStream serverOutputStream;
        private View viewState;

        public VirtualView(Socket socket, Controller controller) {
            this.socket = socket;
            this.controller = controller;
            this.viewState = new VirtualLoginView(this, socket, viewState);
        }

        public void run() {
            try {
                serverInputStream = new ObjectInputStream(socket.getInputStream());
                serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    Event receivedEvent = (Event) serverInputStream.readObject(); // controllare stringa vuota
                    System.out.println(receivedEvent.getClass());
                    controller.handle(receivedEvent, this);
                }

               /* serverOutputStream.close();
                serverInputStream.close();
                socket.close();*/
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }

    public void sendToClient(Event event) {
        try {
            serverOutputStream.writeObject(event);
            serverOutputStream.reset();
        } catch (IOException e) {
            e.printStackTrace(); //TODO Handle closed connection
        }
    }

    public void setViewOwner(Player viewOwner) {
        this.viewOwner = viewOwner;
    }

    public void changeView(View nextState) {
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

}

