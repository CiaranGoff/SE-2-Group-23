package org.example;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

import java.awt.*;

import static java.awt.Color.BLACK;

public class QuaxBoardController {

    private String mode;

    public void setMode(String mode){
        this.mode = mode;
        System.out.println("Game Mode: " + mode);
    }

    @FXML
    public void initialize(){
        // runs when board loads
    }

    private boolean blackTurn = true;

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
    }
}




