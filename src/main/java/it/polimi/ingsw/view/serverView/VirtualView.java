package it.polimi.ingsw.view.serverView;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.ChangeViewEvent;
import it.polimi.ingsw.controller.events.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.clientView.View;

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
        ServerViewState viewState;

        public VirtualView(Socket socket, Controller controller) {
            this.socket = socket;
            this.controller = controller;
            this.viewState = new VirtualLoginView();
        }

        public void run() {
            try {
                serverInputStream = new ObjectInputStream(socket.getInputStream());
                serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    Event receivedEvent = (Event) serverInputStream.readObject(); // controllare stringa vuota
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
        } catch (IOException e) {
            e.printStackTrace(); //TODO Handle closed connection
        }
    }

    public void setViewOwner(Player viewOwner) {
        this.viewOwner = viewOwner;
    }

    public void setNextState(ServerViewState nextState) {
            viewState = nextState;
            sendToClient(new ChangeViewEvent(nextState));
    }


}

