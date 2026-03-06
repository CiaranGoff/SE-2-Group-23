package org.example;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class QuaxBoardController {

    private String mode;

    public void setMode(String mode){
        this.mode = mode;
        System.out.println("Game Mode: " + mode);
    }

    public String getMode(){
        return mode;
    }

    @FXML
    public void initialize(){
        // runs when board loads
    }

    private boolean blackTurn = true;
    private Shape blackFirstMove = null;
    private boolean pieRuleAvailable = true;
    @FXML
    private Shape turnOctagon;

    @FXML
    private Shape turnRhombus;

    @FXML
    private Label turnLabel;

    private void showPieRuleDialog() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pie Rule");
        alert.setHeaderText("PIE Rule");
        alert.setContentText("Do you want to swap with Black's first move?");

        ButtonType swapButton = new ButtonType("Swap");
        ButtonType continueButton = new ButtonType("Continue Normally");

        alert.getButtonTypes().setAll(swapButton, continueButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == swapButton) {
            blackFirstMove.setFill(Color.WHITE);
            pieRuleAvailable = false;
            blackTurn = false;
            updateTurn();
        }
        else {
            pieRuleAvailable = false;
        }
    }


    @FXML
    private void getCellID(MouseEvent event){
        Shape cell = (Shape) event.getSource();

        if((cell.getFill().equals(Color.BLACK) || cell.getFill().equals(Color.WHITE)) && !pieRuleAvailable){
            return;
        }

        if(blackTurn) {
            cell.setFill(Color.BLACK);

            if (blackFirstMove == null) {
                blackFirstMove = cell;
            }
        }
        else {
            cell.setFill(Color.WHITE);
        }
        if (pieRuleAvailable) {
            showPieRuleDialog();
        }
        blackTurn = !blackTurn;
        updateTurn();
    }

    private void updateTurn(){
        if (blackTurn){
            turnLabel.setText("Black's Turn");
            turnOctagon.setFill(Color.BLACK);
            turnRhombus.setFill(Color.BLACK);
        }
        else{
            turnLabel.setText("White's Turn");
            turnOctagon.setFill(Color.WHITE);
            turnRhombus.setFill(Color.WHITE);
        }
    }
}




