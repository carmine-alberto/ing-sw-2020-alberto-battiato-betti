module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens it.polimi.ingsw to javafx.fxml;
    opens it.polimi.ingsw.model.predicates.winConditionsPredicates;
    exports it.polimi.ingsw.view;
}
