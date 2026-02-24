package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
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

    private void loadBoard(ActionEvent event, String mode) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp20050/quax_board.fxml"));
            Parent root = loader.load();

            QuaxBoardController controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in loading board to screen");
        }
    }
}

