package org.example;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.scene.control.Label;

import java.awt.*;

import static java.awt.Color.BLACK;

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
    @FXML
    private Shape turnOctagon;

    @FXML
    private Shape turnRhombus;

    @FXML
    private Label turnLabel;

    @FXML
    private void getCellID(MouseEvent event){
        Shape cell = (Shape) event.getSource();

        if(cell.getFill().equals(Color.BLACK) ||
        cell.getFill().equals(Color.WHITE)){
            return;
        }

        if(blackTurn){
            cell.setFill(Color.BLACK);
        }else{
            cell.setFill(Color.WHITE);
        }

        blackTurn = !blackTurn;
        updateTurn();
    }

    private void updateTurn(){
        if(blackTurn){
            turnLabel.setText("Black's Turn");
            turnOctagon.setFill(Color.BLACK);
            turnRhombus.setFill(Color.BLACK);
        }else{
            turnLabel.setText("White's Turn");
            turnOctagon.setFill(Color.WHITE);
            turnRhombus.setFill(Color.WHITE);
        }
    }
}




