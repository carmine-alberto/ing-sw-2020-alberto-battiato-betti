package it.polimi.ingsw.view.serverView;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static it.polimi.ingsw.GameSettings.GRANULARITY;
import static it.polimi.ingsw.GameSettings.TIMEOUT;

public class VirtualView extends Observable<Event> implements Runnable, Observer<Event> {
        private Socket socket;
        private Thread pingingThread;
        private ObjectInputStream serverInputStream;
        private ObjectOutputStream serverOutputStream;

        private VirtualViewState viewState;
        private String owner;

        private Boolean isClientViewAlive;

        public VirtualView(Socket socket, Controller controller) {
            this.socket = socket;
            this.viewState = new VirtualLoginViewState(this);
            this.isClientViewAlive = true;

            this.addObserver(controller);
        }

        public void run() {
            try {
                serverInputStream = new ObjectInputStream(socket.getInputStream());
                serverOutputStream = new ObjectOutputStream(socket.getOutputStream());

                startPingingThread();

                while (true) {
                    Event receivedEvent = (Event) serverInputStream.readObject();
                    notify(receivedEvent, this);
                }

            } catch (IOException | ClassNotFoundException e) {
                notify(new ClientDisconnectedEvent(), this);
            }
        }

    private void startPingingThread() {
        pingingThread = new Thread(() -> {
            while (isClientViewAlive) {
                isClientViewAlive = false;
                sendToClient(new PingEvent());

                try {
                    Thread.sleep((long) (TIMEOUT * GRANULARITY));
                } catch (InterruptedException e) {}
            }

            notify(new ClientDisconnectedEvent(), this);
        });
        pingingThread.start();
    }

    public void sendToClient(Event event) {
        try {
            serverOutputStream.writeObject(event);
            serverOutputStream.reset();
        } catch (IOException e) {
            notify(new ClientDisconnectedEvent(), this);
        }
    }

    public void changeViewState(VirtualViewState nextState) {
            viewState = nextState;
            sendToClient(new ChangeViewEvent(nextState.toString()));
    }

    public void showMessage(String messageContent) {
            sendToClient(new WarningEvent(messageContent));
    }

    /**
     * This method should be always called by mediator classes (i.e. Controller)
     * after the remaining references to the object have been cleared (removeObserver(), etc...)
     */
    public void terminate() {
        sendToClient(new TerminationEvent());
        try {
            socket.close();
        } catch (IOException e) {
           //The client was terminated successfully and the exception was thrown before the socket could be closed - Nothing to worry about
        }
    }

    public void resetTimerFlag() {
        this.isClientViewAlive = true;
    }

    /**
     * According to the State pattern implemented, event handling is delegated to the class states
     * @param message the observer is notified with
     */
    @Override
    public void update(Event message) {
        viewState.handleUpdate(message);
    }

    public String getOwnerName() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

