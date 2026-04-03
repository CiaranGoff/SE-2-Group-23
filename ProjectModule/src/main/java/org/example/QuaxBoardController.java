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
    private boolean blackTurn = true;
    private Tile blackFirstMove = null;
    private boolean pieRuleAvailable = true;
    //2d arrays used to represent the tiles on the quax board//
    private Tile[][] octagons = new Tile[11][11];
    private Tile[][] rhombuses = new Tile[10][10];
    @FXML
    private Shape turnOctagon;

    @FXML
    private Shape turnRhombus;

    @FXML
    private Label turnLabel;

    public void setMode(String mode) {
        this.mode = mode;
        System.out.println("Game Mode: " + mode);
    }

    public String getMode() {
        return mode;
    }

    @FXML
    public void initialize() {
        initializeShapes();
        linkNeighbours();
    }


    @FXML
    public void initializeShapes() {

        for (int row = 0; row < 11; row++) {
            int rowNum = 11 - row;
            for (int c = 0; c < 11; c++) {
                char colLetter = (char) ('A' + c);
                String id = "OctCell" + rowNum + colLetter;

                octagons[row][c] = new Tile(id, Tile.TileType.OCTAGON, row + 1, c + 1);
            }
        }

        int rhombusCounter = 1;
        for (int row = 0; row < 10; row++) {
            for (int c = 0; c < 10; c++) {
                String id = "RhoCell" + rhombusCounter;
                rhombuses[row][c] = new Tile(id, Tile.TileType.RHOMBUS, row + 1, c + 1);
                rhombusCounter++;
            }
        }

    }

    private void linkNeighbours() {
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                Tile oct = octagons[row][col];

                //adding octagon neighbours to current octagon//
                if (row > 0) oct.getNeighbours().add(octagons[row - 1][col]);
                if (row < 10) oct.getNeighbours().add(octagons[row + 1][col]);
                if (col > 0) oct.getNeighbours().add(octagons[row][col - 1]);
                if (col < 10) oct.getNeighbours().add(octagons[row][col + 1]);

                if (row < 10 && col < 10) {
                    Tile rho = rhombuses[row][col];
                    //these are the rhombuses neighbouring octagons//
                    Tile topLeft = octagons[row][col];
                    Tile topRight = octagons[row][col + 1];
                    Tile botLeft = octagons[row + 1][col];
                    Tile botRight = octagons[row + 1][col + 1];

                    //adding all the neighbouring octagons to the rhombuses neighbours//
                    rho.getNeighbours().addAll(java.util.Arrays.asList(topLeft, topRight, botLeft, botRight));
                    //have to add the rhombus to the octagons neighbours//
                    topLeft.getNeighbours().add(rho);
                    topRight.getNeighbours().add(rho);
                    botLeft.getNeighbours().add(rho);
                    botRight.getNeighbours().add(rho);
                }
            }
        }
    }


     protected Tile findTileById(String id) {
        //search octagons
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (octagons[row][col].getId().equals(id)) {
                    return octagons[row][col];
                }
            }
        }

        //search rhombuses
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (rhombuses[row][col].getId().equals(id)) {
                    return rhombuses[row][col];
                }
            }
        }
        return null;
    }

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
            blackFirstMove.setColor(Tile.TileColor.WHITE);

            Shape pieRuleShape = (Shape) turnOctagon.getScene().lookup("#" + blackFirstMove.getId());
            if (pieRuleShape != null) {
                pieRuleShape.setFill(Color.WHITE);
            }
            blackTurn = true;
        } else {
            blackTurn = false;
        }

        pieRuleAvailable = false;
        updateTurn();

    }


    @FXML
    private void getCellID(MouseEvent event) {
        if (!(event.getSource() instanceof Shape)) {
            return;
        }
        Shape cell = (Shape) event.getSource();

        if ((cell.getFill().equals(Color.BLACK) || cell.getFill().equals(Color.WHITE)) && !pieRuleAvailable) {
            return;
        }

        Tile tile = findTileById(cell.getId());
        if (tile == null) {
            System.out.println("Warning: Clicked shape with ID " + cell.getId() + " is not in the Tile arrays.");
            return;
        }


        Tile.TileColor currentColor = blackTurn ? Tile.TileColor.BLACK : Tile.TileColor.WHITE;
        cell.setFill(blackTurn ? Color.BLACK : Color.WHITE);
        tile.setColor(currentColor);

        if (blackTurn && blackFirstMove == null) {
            blackFirstMove = tile;
        }

        if (checkWin(currentColor)) {
            Alert winner = new Alert(Alert.AlertType.INFORMATION);
            winner.setTitle("Game Over");
            winner.setHeaderText("Congratulations!");
            winner.setContentText((blackTurn ? "Black" : "White") + " wins");

            ButtonType playAgain = new ButtonType("Play Again");
            ButtonType exitGame = new ButtonType("Exit Game");
            winner.getButtonTypes().setAll(playAgain, exitGame);
            Optional<ButtonType> result = winner.showAndWait();
            if (result.isPresent() && result.get() == playAgain) {
                resetGame();
            }else{
                javafx.application.Platform.exit();
            }
            return;
        }

        if (pieRuleAvailable && blackTurn) {
            showPieRuleDialog();
        } else {
            blackTurn = !blackTurn;
            updateTurn();
        }
    }

    private void updateTurn() {
        if (blackTurn) {
            turnLabel.setText("Black's Turn");
            turnOctagon.setFill(Color.BLACK);
            turnRhombus.setFill(Color.BLACK);
        } else {
            turnLabel.setText("White's Turn");
            turnOctagon.setFill(Color.WHITE);
            turnRhombus.setFill(Color.WHITE);
        }
    }

    protected boolean checkWin(Tile.TileColor color) {
        boolean hasStart = false;
        boolean hasEnd = false;

        if (color == Tile.TileColor.BLACK) {
            for (int i = 0; i < 11; i++) {
                if (octagons[0][i].getColor() == color) hasStart = true;
                if (octagons[10][i].getColor() == color) hasEnd = true;
            }
        } else {
            for (int i = 0; i < 11; i++) {
                if (octagons[i][0].getColor() == color) hasStart = true;
                if (octagons[i][10].getColor() == color) hasEnd = true;
            }
        }

        if (!hasStart || !hasEnd) return false;

        boolean[][] visitedOct = new boolean[11][11];
        boolean[][] visitedRho = new boolean[10][10];

        if (color == Tile.TileColor.BLACK) {
            for (int c = 0; c < 11; c++) {
                if (octagons[0][c].getColor() == color) {
                    if (checkChain(octagons[0][c], color, visitedOct, visitedRho)) return true;
                }
            }
        } else {
            for (int r = 0; r < 11; r++) {
                if (octagons[r][0].getColor() == color) {
                    if (checkChain(octagons[r][0], color, visitedOct, visitedRho)) return true;
                }
            }
        }
        return false;
    }

    protected boolean checkChain(Tile tile, Tile.TileColor color, boolean[][] visitedOct, boolean[][] visitedRho) {
        if (tile == null || tile.getColor() != color) return false;

        int row = tile.getTileRow() - 1;
        int col = tile.getTileCol() - 1;

        if (tile.getType() == Tile.TileType.OCTAGON) {
            if (visitedOct[row][col]) return false;
            visitedOct[row][col] = true;
            if (color == Tile.TileColor.BLACK && tile.getTileRow() == 11) return true;
            if (color == Tile.TileColor.WHITE && tile.getTileCol() == 11) return true;
        } else {
            if (visitedRho[row][col]) return false;
            visitedRho[row][col] = true;
        }

        for (Tile neighbor : tile.getNeighbours()) {
            if (checkChain(neighbor, color, visitedOct, visitedRho)) return true;
        }
        return false;
    }

    protected void resetGame() {
        //rest variables//
        blackTurn = true;
        blackFirstMove = null;
        pieRuleAvailable = true;

        //reset the colour of the octagons//
        for (int r = 0; r < 11; r++) {
            for (int c = 0; c < 11; c++) {
                octagons[r][c].setColor(Tile.TileColor.EMPTY);
                Shape s = (Shape) turnOctagon.getScene().lookup("#" + octagons[r][c].getId());
                if (s != null) {
                    s.setFill(Color.web("#e9c218"));
                }
            }
        }

        //reset the colour of the rhombuses
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                rhombuses[r][c].setColor(Tile.TileColor.EMPTY);
                Shape s = (Shape) turnOctagon.getScene().lookup("#" + rhombuses[r][c].getId());
                if (s != null) {
                    s.setFill(Color.web("#eeae0b"));
                }
            }
        }

        updateTurn();
    }
}




