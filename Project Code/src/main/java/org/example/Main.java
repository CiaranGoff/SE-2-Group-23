package org.example;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/SelectGameMode.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Quax");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}