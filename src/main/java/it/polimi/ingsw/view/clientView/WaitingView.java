package it.polimi.ingsw.view.clientView;

import it.polimi.ingsw.view.clientView.View;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WaitingView extends View {
    public WaitingView(Stage stage, Socket clientSocket, View viewState) {
        super(stage, clientSocket, viewState);
    }

    @Override
    public void render() {
        Integer NUM_OF_PARTICLES = 12;
        Circle particle = null;
        Circle particlePath = new Circle(mainStage.getWidth() / 2, mainStage.getHeight() / 2, 60);
        Float waitTime = 0.001F;

        Label waitForPlayersLabel = new Label("Waiting for other players"); //TODO Add label to the scene

        List<Circle> particles = new ArrayList<>();
        for (Integer i = 0; i < NUM_OF_PARTICLES; i++) {
            particle = new Circle(mainStage.getWidth() / 2, mainStage.getHeight() / 2 + 60, 10, Color.TRANSPARENT);
            animateParticle(particle, particlePath , waitTime);
            particles.add(particle);
            waitTime += 1.5F/NUM_OF_PARTICLES;
        }
        Group animationContainer = new Group();
        animationContainer.getChildren().addAll(particles);

        mainStage.getScene().setRoot(animationContainer);
    }

    private void animateParticle(Node particle, Shape path, Float timeBeforeNext){
        PathTransition transition = new PathTransition();
        transition.setPath(path);
        transition.setNode(particle);
        transition.setDuration(Duration.seconds(1.5));
        transition.setCycleCount(Transition.INDEFINITE);
        transition.setInterpolator(Interpolator.LINEAR);

        KeyFrame k = new KeyFrame(Duration.seconds(timeBeforeNext), e -> {((Circle) particle).setFill(new Color(0.45, 0.45, 0.45, 1)); transition.play();});
        new Timeline(k).play();
    }
}
