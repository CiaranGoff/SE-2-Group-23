package org.example;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuaxBoardController {

    private String mode;
    private boolean blackTurn = true;
    private boolean botDelay = false;
    private Tile blackFirstMove = null;
    private boolean pieRuleAvailable = true;
    //2d arrays used to represent the tiles on the quax board//
    private final Tile[][] octagons = new Tile[11][11];
    private final Tile[][] rhombuses = new Tile[10][10];
    private final boolean showStrategy = false;
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
        if(botDelay) {
            return;
        }

        if (!(event.getSource() instanceof Shape cell)) {
            return;
        }

        if(this.mode.equals("BOT") && !blackTurn) {
            return;
        }

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

        if (currentColor == Tile.TileColor.BLACK) {
            lastBlackPlaced = tile;
        } else {
            lastWhitePlaced = tile;
        }

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
            botDelay = true;
            PauseTransition del = new PauseTransition(Duration.seconds(1));
            del.setOnFinished(e -> {
                botMove();
                botDelay = false;
            });
            del.play();
        }
    }

    private void updateTurn() {
        if (blackTurn) {
            turnLabel.setText("BLACK'S TURN");
            turnOctagon.setFill(Color.BLACK);
            turnRhombus.setFill(Color.BLACK);
        } else {
            turnLabel.setText("WHITE'S TURN");
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
        botDelay = false;

        lastBlackPlaced = null;
        lastWhitePlaced = null;
        guaranteeConnection.clear();

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
        alert.setContentText(message + " has won. Do you want to restart or exit");

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
    protected Tile botMove() {
        Tile move;

        //first move logic//
        if(lastWhitePlaced == null){
            move = botFirstMove();
            if(move != null){
                return executeBotMove(move);
            }
        }

        //reaction to opponent//
        move = claimGuaranteedConnection();
        if(move != null){
            return executeBotMove(move);
        }

        //set up a guaranteed connection//
        move = secureConnection();
        if(move != null){
            return executeBotMove(move);
        }

        //if black is close to winning, block them//
        move = checkBlockOpponent();
        if(move != null){
            return executeBotMove(move);
        }

        move = buildGuaranteeConnection();
        if(move != null){
            return executeBotMove(move);
        }

        //advance bots shortest path//
        move = advanceOwnPath();
        if(move != null){
            return executeBotMove(move);
        }

        move = fallBackMove();
        if(move != null){
            return executeBotMove(move);
        }

        //this occurs if board is full//
        System.out.println("ERROR: bot couldnt find a move");
        blackTurn = true;
        updateTurn();
        return null;
    }

    private Tile executeBotMove(Tile move) {
        applyMove(move);
        return move;
    }

    protected void applyMove(Tile tile) {
        tile.setColor(Tile.TileColor.WHITE);
        lastWhitePlaced = tile;

        Shape shape = (Shape) turnOctagon.getScene().lookup("#" + tile.getId());
        if (shape != null) {
            shape.setFill(Color.WHITE);
        }

        if (checkWin(Tile.TileColor.WHITE)) {
            javafx.application.Platform.runLater(() -> showWinnerAlert("White , Bot"));
        } else {
            blackTurn = true;
            updateTurn();
        }
    }

    //calculating best path using dijkstras algorithm//
    protected Tile[] calculateBestPath(Tile.TileColor color) {
        Tile.TileColor opp = (color == Tile.TileColor.WHITE) ? Tile.TileColor.BLACK : Tile.TileColor.WHITE;
        java.util.PriorityQueue<PathNode> pq = new java.util.PriorityQueue<>();
        java.util.Map<Tile, Tile> parents = new java.util.HashMap<>();
        java.util.Map<Tile, Integer> dist = new java.util.HashMap<>();

        if(color == Tile.TileColor.WHITE) {
            //deals with white west to east//
            for (int row = 0; row < 11; row++) {
                Tile start = octagons[row][0];
                if (start.getColor() != opp) {
                    int firstCost = (start.getColor() == color) ? 0 : 1;
                    dist.put(start, firstCost);
                    pq.add(new PathNode(start, firstCost));
                }
            }
        }else{
            for(int col = 0; col < 11; col++){
                Tile start = octagons[0][col];
                if(start.getColor() != opp){
                    int firstCost = (start.getColor() == color) ? 0 : 1;
                    dist.put(start, firstCost);
                    pq.add(new PathNode(start, firstCost));
                }
            }
        }

        while (!pq.isEmpty()) {
            PathNode curr = pq.poll();
            Tile tile = curr.tile;

            if(color == Tile.TileColor.WHITE) {
                if (tile.getTileCol() == 11 && tile.getType() == Tile.TileType.OCTAGON) {
                    return reconstructPath(tile, parents);
                }
            }else{
                if(tile.getTileRow() == 11 && tile.getType() == Tile.TileType.OCTAGON) {
                    return reconstructPath(tile, parents);
                }
            }

            for (Tile t : tile.getNeighbours()) {
                if (t.getColor() == opp) {
                    continue;
                }
                int weight = (t.getColor() == color) ? 0 : 1;

                //rhombuses have a higher weight as they have less neighbours//
                if(isEmpty(t) && t.getType() == Tile.TileType.RHOMBUS){
                    weight = 3;
                }
                //this check avoids backtracking with our guaranteeConnection method//
                if(isEmpty(t) && guaranteeConnection.containsKey(t)) {
                    weight = 0;
                }

                int distance = dist.get(tile) + weight;

                if (distance < dist.getOrDefault(t, 999)) {
                    dist.put(t, distance);
                    parents.put(t, tile);
                    pq.add(new PathNode(t, distance));
                }
            }
        }
        return null;
    }

    protected Tile[] reconstructPath(Tile endTile, java.util.Map<Tile, Tile> parents) {
        java.util.List<Tile> path = new java.util.LinkedList<>();
        Tile curr = endTile;

        while (curr != null) {
            path.add(0, curr);
            curr = parents.get(curr);
        }
        return path.toArray(new Tile[0]);
    }

    private Tile lastWhitePlaced = null;
    private Tile lastBlackPlaced = null;

    private java.util.Map<Tile, Tile> guaranteeConnection = new java.util.HashMap<>();

    protected Tile secureConnection() {
        if(lastBlackPlaced == null || lastBlackPlaced.getType() != Tile.TileType.OCTAGON){
            return null;
        }

        Tile cutOffWhite = null;
        int blackRow = lastBlackPlaced.getTileRow() - 1;
        int blackCol = lastBlackPlaced.getTileCol() - 1;

        //check to see if the tile is placed directly left/right of bots tile//
       if(blackCol > 0 && octagons[blackRow][blackCol - 1].getColor() == Tile.TileColor.WHITE){
           cutOffWhite = octagons[blackRow][blackCol - 1];
       }else if(blackCol < 10 && octagons[blackRow][blackCol + 1].getColor() == Tile.TileColor.WHITE){
           cutOffWhite = octagons[blackRow][blackCol + 1];
       }

       if(cutOffWhite == null){
           return null;
       }

       int whiteRow = cutOffWhite.getTileRow() - 1;
       int whiteCol = cutOffWhite.getTileCol() - 1;

        boolean aboveValid = false;
        Tile aboveOct1 = null;
        Tile aboveOct2 = null;
        Tile aboveRho = null;

        //checking the above scenario//
        if (whiteRow - 1 >= 0) {
            int currentRow = whiteRow - 1;
            aboveOct1 = octagons[currentRow][blackCol]; //octagon above white//
            aboveOct2 = octagons[currentRow][whiteCol]; //octagon above black//
            aboveRho = rhombuses[currentRow][Math.min(whiteCol, blackCol)]; //rhombus connecting//

            if (isEmpty(aboveOct1) && isEmpty(aboveOct2) && isEmpty(aboveRho)
                    && !guaranteeConnection.containsKey(aboveOct2)
                    && !guaranteeConnection.containsKey(aboveRho)
                    && !guaranteeConnection.containsKey(aboveOct1)) {
                aboveValid = true;
            }
        }

        boolean belowValid = false;
        Tile belowOct1 = null;
        Tile belowOct2 = null;
        Tile belowRho = null;

        if (whiteRow + 1 < 11) {
            int currentRow = whiteRow + 1;
            belowOct1 = octagons[currentRow][blackCol];
            belowOct2 = octagons[currentRow][whiteCol];
            belowRho = rhombuses[whiteRow][Math.min(whiteCol, blackCol)];

            if (isEmpty(belowOct1) && isEmpty(belowOct2) && isEmpty(belowRho)
                    && !guaranteeConnection.containsKey(belowOct2)
                    && !guaranteeConnection.containsKey(belowRho)
                    && !guaranteeConnection.containsKey(belowOct1)){
                belowValid = true;
            }
        }

        //no connections available//
        if (!aboveValid && !belowValid) {
            return null;
        }

        //if both options free we will pick one furthest from the edge//
        boolean chooseAbove = false;
        if (aboveValid && belowValid) {
            int aboveDist = Math.min(whiteRow - 1, 10 - (whiteRow - 1));
            int belowDist = Math.min(whiteRow + 1, 10 - (whiteRow + 1));
            chooseAbove = aboveDist >= belowDist;
        } else if (aboveValid) {
            //only one valid case//
            chooseAbove = true;
        }

        if (chooseAbove) {
            guaranteeConnection.put(aboveOct2, aboveRho);
            guaranteeConnection.put(aboveRho, aboveOct2);
            guaranteeConnection.put(aboveOct1, aboveRho);
            return aboveOct1;
        } else {
            guaranteeConnection.put(belowOct2, belowRho);
            guaranteeConnection.put(belowRho, belowOct2);
            guaranteeConnection.put(belowOct1, belowRho);
            return belowOct1;
        }

    }
    protected boolean isEmpty(Tile tile) {
        return Tile.TileColor.EMPTY.equals(tile.getColor());
    }

    protected Tile claimGuaranteedConnection(){
        Tile linkTile = null;
        Tile triggerTile = null;

        for(java.util.Map.Entry<Tile, Tile> entry : guaranteeConnection.entrySet()){
            if(isBlack(entry.getKey()) && isEmpty(entry.getValue())){
                linkTile = entry.getValue();
                triggerTile = entry.getKey();
                break;
            }else if(lastBlackPlaced != null && entry.getKey().getTileRow() == lastBlackPlaced.getTileRow() && entry.getKey().getTileCol() == lastBlackPlaced.getTileCol()){
                if(isEmpty(entry.getValue())){
                    linkTile = entry.getValue();
                    triggerTile = entry.getKey();
                    break;
                }
            }
        }

      if (linkTile != null) {
          guaranteeConnection.remove(linkTile);
          guaranteeConnection.remove(triggerTile);
          return linkTile;
      }
      return null;
    }

    protected Tile botFirstMove(){
        Tile start = octagons[6][0];
        if(!isEmpty(start)){
            start = octagons[4][0];
        }
        return start;
    }

    protected Tile fallBackMove(){
        for(int r = 0; r < 11; r++){
            for(int c = 0; c < 11; c++){
                Tile tile = octagons[r][c];
                if(isEmpty(tile)){
                    return tile;
                }
            }
        }
        return null;
    }

    protected Tile blockOpponentPath(){
        Tile[] blackPath = calculateBestPath(Tile.TileColor.BLACK);
        Tile[] whitePath = calculateBestPath(Tile.TileColor.WHITE);

        if(blackPath == null){
            return null;
        }

        Tile bestTile = null;
        int bestScore = -999;
        for(Tile t : blackPath){
            if(!isEmpty(t) || guaranteeConnection.containsKey(t)){
                continue;
            }

            int row = t.getTileRow() - 1;
            int score = 0;

            boolean capLadder = false;
            for(Tile neighbour : t.getNeighbours()){
                if(neighbour != null && isBlack(neighbour)){
                    capLadder = true;
                    break;
                }
            }

            //ensures we cap head of the black ladder//
            if(capLadder){
                score += 100;
            }

            int rowCentrality = 5 - Math.abs(5 - row);
            score += (rowCentrality * 2);

            if(t.getType() == Tile.TileType.OCTAGON){
                score += 10;
            }

            //checks to see if blocking black also advances the bots shortest path//
            if(whitePath != null){
                for(Tile w : whitePath){
                    if(w.equals(t)){
                        score += 20;
                    }
                }
            }

            if(score > bestScore){
                bestScore = score;
                bestTile = t;
            }
        }
        return bestTile;
    }

    protected Tile checkBlockOpponent(){
        Tile[] blackPath = calculateBestPath(Tile.TileColor.BLACK);
        Tile[] whitePath = calculateBestPath(Tile.TileColor.WHITE);
        if(blackPath == null){
            return null;
        }

        int blackEmptyCount = 0;
        int whiteEmptyCount = 0;
        if(blackPath != null){
            for(Tile t : blackPath){
                if(isEmpty(t)){
                    blackEmptyCount++;
                }
            }
        }

        if(whitePath != null){
            for(Tile t : whitePath){
                if(isEmpty(t) && !guaranteeConnection.containsKey(t)){
                    whiteEmptyCount++;
                }
            }
        }else{
            //whites path is cut off//
            whiteEmptyCount = 99;
        }

        if(blackEmptyCount <= whiteEmptyCount + 2){
            return blockOpponentPath();
        }
        return null;
    }

    protected Tile advanceOwnPath(){
        Tile[] path = calculateBestPath(Tile.TileColor.WHITE);
        if(path == null){
            return null;
        }

        //check for a free tile thats not in guaranteeConnection//
        for(Tile t : path){
            if(isEmpty(t) && !guaranteeConnection.containsKey(t)){
                return t;
            }
        }

        //if all tiles are within guaranteeConnection, pick one and then delete it//
        for(Tile t : path){
            if(isEmpty(t)){
                guaranteeConnection.remove(t);
                return t;
            }
        }
        return null;
    }

    protected Tile buildGuaranteeConnection(){
        Tile[] path = calculateBestPath(Tile.TileColor.WHITE);
        if(path == null){
            return null;
        }

        for(int i = 0; i < path.length; i++){
            Tile candidate = path[i];

            if(!isEmpty(candidate) || guaranteeConnection.containsKey(candidate)){
                continue;
            }

            for(Tile currWhite : getAllTilesOfColor(Tile.TileColor.WHITE)){
                int sharedNeighbours = 0;
                Tile neighbour1 = null;
                Tile neighbour2 = null;

                for(Tile n1 : candidate.getNeighbours()){
                    for(Tile n2 : currWhite.getNeighbours()){
                        if(n1.equals(n2) && isEmpty(n1) && !guaranteeConnection.containsKey(n1)){
                            sharedNeighbours++;
                            if(neighbour1 == null){
                                neighbour1 = n1;
                            }else{
                                neighbour2 = n1;
                            }
                        }
                    }
                }

                if(sharedNeighbours >= 2){
                    guaranteeConnection.put(neighbour1, neighbour2);
                    guaranteeConnection.put(neighbour2, neighbour1);
                    return candidate;
                }
        }
        }
        return null;
    }

    protected List<Tile> getAllTilesOfColor(Tile.TileColor color){
        List<Tile> tiles = new ArrayList<>();

        for(int r = 0; r < 11; r++){
            for(int c = 0; c < 11; c++){
                if(octagons[r][c].getColor() == color){
                    tiles.add(octagons[r][c]);
                }
            }
        }

        for(int r = 0; r < 10; r++){
            for(int c = 0; c < 10; c++){
                if(rhombuses[r][c].getColor() == color){
                    tiles.add(rhombuses[r][c]);
                }
            }
        }
        return tiles;
    }

    protected boolean isBlack(Tile tile){
        return tile.getColor() == Tile.TileColor.BLACK;
    }

    protected boolean isWhite(Tile tile){
        return tile.getColor() == Tile.TileColor.WHITE;
    }
}