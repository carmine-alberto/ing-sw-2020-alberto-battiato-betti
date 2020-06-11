package it.polimi.ingsw;

import it.polimi.ingsw.cview.ViewState;
import it.polimi.ingsw.cview.clientView.LoginViewState;
import it.polimi.ingsw.model.FieldCell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileDescriptor;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("Ce la faremo! " + Client.class.getClassLoader().getResource("copertina_santorini_2016.jpg"));

        View.launchView();

    }
}