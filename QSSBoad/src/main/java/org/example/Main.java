package org.example;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Circle circle1 = new Circle(50);
        circle1.setLayoutX(400);
        circle1.setLayoutY(100);

        Circle circle2 = new Circle(30, Color.GREEN);
        circle2.setLayoutX(150);
        circle2.setLayoutY(150);

        Group root = new Group(circle1, circle2);

        Scene scene = new Scene(root, 2000, 900);
        stage.setScene(scene);
        stage.setTitle("Group Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}