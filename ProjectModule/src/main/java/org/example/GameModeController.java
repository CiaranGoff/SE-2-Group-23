package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameModeController {

    @FXML
    private void handleHuman(ActionEvent event) throws Exception {
        loadBoard(event, "HUMAN");
    }

    @FXML
    private void handleBot(ActionEvent event) throws Exception {
        loadBoard(event, "BOT");
    }

    private void loadBoard(ActionEvent event, String mode) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp20050/quax_board.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Quax - " + mode);
        stage.show();
    }
}
