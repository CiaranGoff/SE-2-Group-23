package org.example;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

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

    @FXML
    private void getCellID(MouseEvent event){
        System.out.println("Clicked in mode: " + mode);
    }
}




