module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
}
