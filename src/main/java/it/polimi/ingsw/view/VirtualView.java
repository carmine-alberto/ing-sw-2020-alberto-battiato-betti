package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.Event;
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

        public VirtualView(Socket socket, Controller controller) {
            this.socket = socket;
            this.controller = controller;
        }

        public void run() {
            try {
                serverInputStream = new ObjectInputStream(socket.getInputStream());
                serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

                Event receivedEvent = (Event) serverInputStream.readObject(); // controllare stringa vuota
                System.out.println((receivedEvent));

                serverOutputStream.close();
                serverInputStream.close();
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }

    public void setViewOwner(Player viewOwner) {
        this.viewOwner = viewOwner;
    }
}

