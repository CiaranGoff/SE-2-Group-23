package org.example;
import javafx.scene.control.ToggleButton;
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
    private boolean showStrategy = false;
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

        clearHighlights();

        if (showStrategy) {
            highlightPath();
        }

        if (!(event.getSource() instanceof Shape)) {
            return;
        }
        Shape cell = (Shape) event.getSource();

        if ((cell.getFill().equals(Color.BLACK) || cell.getFill().equals(Color.WHITE)) && (!pieRuleAvailable || this.mode.equals("BOT"))) {
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
            showWinnerAlert(blackTurn ? "Black" : "White");
            return;
        }

        if (pieRuleAvailable && blackTurn && !this.mode.equals("BOT")) {
            showPieRuleDialog();
        } else {
            blackTurn = !blackTurn;
            updateTurn();
        }

        if (this.mode.equals("BOT") && !blackTurn) {
            highlightPath();
            botMove();
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

    private void showWinnerAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        alert.setContentText(message + "has won. Do you want to restart or exit");

        ButtonType restart = new ButtonType("Restart");
        ButtonType exit = new ButtonType("Exit");
        alert.getButtonTypes().setAll(restart, exit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == exit) {
            javafx.application.Platform.exit();
        } else {
            resetGame();
        }
    }

    //BEGINNING OF BOT METHODS//
    private Tile botMove() {
        Tile[] path = calculateBestPath(Tile.TileColor.WHITE);
        if (path != null) {
            for (Tile t : path) {
                if (t.getColor() == Tile.TileColor.EMPTY) {
                    applyMove(t);
                    return t;
                }
            }
        }
        return null;
    }

    private void applyMove(Tile tile) {
        tile.setColor(Tile.TileColor.WHITE);
        Shape shape = (Shape) turnOctagon.getScene().lookup("#" + tile.getId());
        if (shape != null) {
            shape.setFill(Color.WHITE);
        }

        if (checkWin(Tile.TileColor.WHITE)) {
            showWinnerAlert("White , Bot");
        } else {
            blackTurn = true;
            updateTurn();
        }
    }

    //calculating best path using dijkstras algorithm//
    protected Tile[] calculateBestPath(Tile.TileColor color){
        //can be used to calculate HUMAN moves in order to block their path//
        Tile.TileColor opp = (color == Tile.TileColor.WHITE) ? Tile.TileColor.BLACK : Tile.TileColor.WHITE;
        //used to store the next best available tile//
        java.util.PriorityQueue<PathNode> pq = new java.util.PriorityQueue<>();
        //used to ensure we have a track of the best path//
        java.util.Map<Tile, Tile> parents = new java.util.HashMap<>();
        //contains the distance and the correspond tile//
        java.util.Map<Tile, Integer> dist = new java.util.HashMap<>();

        for(int row = 0; row < 11; row++){
            Tile start = octagons[row][0];
            if(start.getColor() != Tile.TileColor.BLACK){
                int firstCost = (start.getColor() == color) ? 0 : 1;
                dist.put(start, firstCost);
                pq.add(new PathNode(start, firstCost));
            }
        }

        while(!pq.isEmpty()){
            PathNode curr = pq.poll();
            Tile tile = curr.tile;

            if(tile.getTileCol() == 11 && tile.getType() == Tile.TileType.OCTAGON){
                return reconstructPath(tile, parents);
            }

            for(Tile t : tile.getNeighbours()){
                if(t.getColor() == opp){
                    continue;
                }
                int weight = (t.getColor() == color) ? 0 : 1;
                int distance = dist.get(tile) + weight;

                if(distance < dist.getOrDefault(t, 999)){
                    dist.put(t, distance);
                    parents.put(t, tile);
                    pq.add(new PathNode(t, distance));
                }
            }
        }
        return null;
    }

    protected Tile[] reconstructPath(Tile endTile, java.util.Map<Tile, Tile> parents){
        java.util.List<Tile> path = new java.util.LinkedList<>();
        Tile curr = endTile;

        while(curr != null){
            path.add(0, curr);
            curr = parents.get(curr);
        }
        return path.toArray(new Tile[0]);
    }

    @FXML
    private ToggleButton strategyToggle;

    @FXML
    private void handleShowStrategyToggle() {
        showStrategy = strategyToggle.isSelected();

        if (!showStrategy) {
            clearHighlights();
            strategyToggle.setText("Show Strategy");
        } else {
            strategyToggle.setText("Hide Strategy");
            highlightPath();
        }
    }

    private void highlightPath() {
        if (!showStrategy) {return;}
        if (!"BOT".equalsIgnoreCase(mode)) {return;}

        clearHighlights();

        Tile[] nextMove = calculateBestPath(Tile.TileColor.WHITE);

        if (nextMove != null) {
            for (Tile tile : nextMove) {
                Shape shape = (Shape) turnOctagon.getScene().lookup("#" + tile.getId());

                if (shape != null && !(shape.getFill() == Color.WHITE) && !(shape.getFill() == Color.BLACK)) {
                    shape.setFill(Color.LIGHTBLUE);
                }
            }
        }
    }

    private void clearHighlights() {

        // reset octagons
        for (int r = 0; r < 11; r++) {
            for (int c = 0; c < 11; c++) {
                Tile t = octagons[r][c];

                if (t.getColor() == Tile.TileColor.EMPTY) {
                    Shape s = (Shape) turnOctagon.getScene().lookup("#" + t.getId());
                    if (s != null) {
                        s.setFill(Color.web("#e9c218"));
                    }
                }
            }
        }

        // reset rhombuses
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Tile t = rhombuses[r][c];

                if (t.getColor() == Tile.TileColor.EMPTY) {
                    Shape s = (Shape) turnOctagon.getScene().lookup("#" + t.getId());
                    if (s != null) {
                        s.setFill(Color.web("#eeae0b"));
                    }
                }
            }
        }
    }
}








