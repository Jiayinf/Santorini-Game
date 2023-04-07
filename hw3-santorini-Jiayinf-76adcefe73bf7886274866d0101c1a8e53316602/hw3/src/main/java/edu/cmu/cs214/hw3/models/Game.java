package edu.cmu.cs214.hw3.models;

import edu.cmu.cs214.hw3.cards.God;
import edu.cmu.cs214.hw3.utils.Configurator;
import edu.cmu.cs214.hw3.utils.RoundAction;
import edu.cmu.cs214.hw3.utils.WorkerType;

import java.util.List;

public class Game {
    private final Board board;
    private Player playerA;
    private Player playerB;
    private Player currentPlayer;
    private Configurator configurator;
    private RoundAction roundAction;
    private String message;
    private String phase;
    private boolean isRunning = false;
    private Player winner = null;



    public Game() {
        this.board = new Board();
        this.roundAction = new RoundAction();
        this.configurator = new Configurator(board);
        this.phase = "start game";
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Player getOpponentPlayer() {
        return this.currentPlayer == playerA ? playerB : playerA;
    }

    public Board getBoard() {
        return this.board;
    }

    public List<Game> getHistory(){
        return this.getHistory();
    }

    public String getMessage() { return this.message; }

    public String getPhase() { return this.phase; }

    public void setMessage(String newMsg) { this.message = newMsg; }
    public void setPhase(String newPhase) { this.phase = newPhase; }

    public RoundAction getRoundAction() { return this.roundAction; }

    public boolean getIsRunning() { return this.isRunning; }

    public Player getWinner() { return this.winner; }

    /**
     * Initialize the game with two players and choose a starting player.
     *
     * @param nameA Name of the first player
     * @param nameB Name of the second player
     * @return True if game can be successfully initialized, false if players are missing
     */
   
    public boolean initGame(String nameA, String nameB) {
        if(nameA == null || nameB == null) {
            message = "Sorry, game needs at least 2 players to start.";
            return false;
        }

        playerA = new Player("A" + nameA);
        playerB = new Player("B" + nameB);

        this.currentPlayer = playerA;
        phase = "choose god";
        return true;
    }

    /**
     * Initialize the god powers for the two players.
     *
     * @param godNameA Name of the god chosen by the first player
     * @param godNameB Name of the god chosen by the second player
     * @return True if gods are successfully initialized; False otherwise
     */
    public boolean chooseGod(String godNameA, String godNameB) throws Exception {
        String basePath = "edu.cmu.cs214.hw3.cards.";
        God godA, godB;

        String classNameA = basePath + godNameA;
        String classNameB = basePath + godNameB;
        
        godA = (God) Class.forName(classNameA).getDeclaredConstructor().newInstance();
        godB= (God) Class.forName(classNameB).getDeclaredConstructor().newInstance();
        
        this.currentPlayer.setGod(godA);
        getOpponentPlayer().setGod(godB);

        configurator.matchPickStartingPositionURL();
        phase = "running";
        message = "Now it's " + currentPlayer.getName().substring(1) + "'s turn!";
        return true;
    }

    /**
     * Pick a starting position for worker, which can only stand on an unoccupied cell.
     *
     * @param position Starting position for the current worker
     * @return True if worker can be placed on this position; false otherwise
     */
    public boolean pickStartingPosition(int[] position) {

        boolean success = true;
        Worker workerA = this.currentPlayer.getWorkerByType(WorkerType.TYPE_A);
        Worker workerB = this.currentPlayer.getWorkerByType(WorkerType.TYPE_B);
        Worker workerOA = getOpponentPlayer().getWorkerByType(WorkerType.TYPE_A);
        Worker workerOB = getOpponentPlayer().getWorkerByType(WorkerType.TYPE_B);

        // Validation check for only allowing picking four workers in total
        if(workerA.getCurPosition() != null && workerB.getCurPosition() != null
            && workerOA.getCurPosition() != null && workerOB.getCurPosition() != null) {
            message = "All workers are set. game is ready to go! Enjoy!";
            return false;
        }

        if(workerA.getCurPosition() == null) {
            success = workerA.setCurPosition(board.getCell(position[0], position[1]));
        } else if (workerB.getCurPosition() == null) {
            success = workerB.setCurPosition(board.getCell(position[0], position[1]));
        }

        if(!success) {
            message = "Sorry, worker cannot stands on an occupied space.";
            return false;
        }

        if (workerA.getCurPosition() != null && workerB.getCurPosition() != null) {
            takeTurns();
            if (workerOA.getCurPosition() != null && workerOB.getCurPosition() != null) {
                configurator.matchChooseWorkerURL();
                phase = "Choose Worker";
                isRunning = true;
                
            }
        }
        return true;
    }

    /**
     * Choose a worker based on the selected position
     *
     * @param curPos current position for choosing a worker
     * @return True if worker exists; false otherwise
     */
    public boolean chooseWorker(int[] curPos) {
        if (!isRunning) return false;

        Cell curCell = board.getCell(curPos[0], curPos[1]);
        Worker worker = currentPlayer.getWorkerByPosition(curCell);
        if(worker == null) {
            message = "Please choosing a worker to start moving!";
            return false;
        }
        roundAction.setRoundWorker(worker);
        phase = "Set Move";
        return true;
    }

    /**
     * Get a list of movable cells for the current selected worker
     *
     * @return The list of movable cells
     */
    public List<Cell> computeMovableCells() {
        Worker roundWorker = roundAction.getRoundWorker();
        God currentGod = currentPlayer.getGod();
        God opponentGod = getOpponentPlayer().getGod();

        List<Cell> movableCells = currentGod.getMovableCells(roundWorker, this);
        // Apply opponent's power from last round
        movableCells = opponentGod.applyOpponentPowerToMove(movableCells, roundWorker);

        roundAction.setRoundPossibleMoves(movableCells);
        configurator.matchRoundMoveURL();

        if(movableCells.size() == 0) {
            message = "No available moves, please try the other worker...";
            phase = "Choose Worker";
        }
        return movableCells;
    }

    /**
     * Move the current worker to the selected position
     *
     * @param movePos selected move to position
     * @return True if move is successful; false otherwise
     */
    public boolean roundMove(int[] movePos) {
        Worker roundWorker = roundAction.getRoundWorker();
        List<Cell> possibleMoves = roundAction.getRoundPossibleMoves();
        Cell moveTo = board.getCell(movePos[0], movePos[1]);

        if(possibleMoves.size() == 0 || !possibleMoves.contains(moveTo)) {
            // If moving fails, choose another cell to move to
            message = "Oops! You (" + currentPlayer.getName().substring(1) +
                    ") cannot move to this cell [" + movePos[0] + ", " +
                    movePos[1] +"].";
            return false;
        }

        God currentGod = currentPlayer.getGod();
        currentGod.doMove(roundWorker, moveTo, this);

        // phase = "Set Builds";

        return true;
    }

    /**
     * Get a list of buildable cells for the current selected worker
     *
     * @return The list of buildable cells
     */
    public List<Cell> computeBuildableCells() {
        Worker roundWorker = roundAction.getRoundWorker();
        God currentGod = currentPlayer.getGod();
        God opponentGod = getOpponentPlayer().getGod();

        List<Cell> buildableCells = currentGod.getBuildableCells(roundWorker, this);
        // Apply opponent's power from last round
        buildableCells = opponentGod.applyOpponentPowerToBuild(buildableCells);

        roundAction.setRoundPossibleBuilds(buildableCells);
        configurator.matchRoundBuildURL();
        return buildableCells;
    }

    /**
     * Build blocks on the selected position
     *
     * @param buildPos selected build on position
     * @return True if build is successful; false otherwise
     */
    public boolean roundBuild(int[] buildPos) {
        List<Cell> possibleBuilds = roundAction.getRoundPossibleBuilds();
        Cell buildOn = board.getCell(buildPos[0], buildPos[1]);

        if(possibleBuilds.size() == 0 || !possibleBuilds.contains(buildOn)) {
            // If moving fails, choose another cell to move to
            message = "Sorry! You (" + currentPlayer.getName().substring(1) +
                    ") cannot build on this cell [" + buildPos[0] + ", "
                    + buildPos[1] + "].";
            return false;
        }

        God currentGod = currentPlayer.getGod();
        currentGod.doBuild(buildOn);

        
        return true;
    }

    /**
     * Player take turns after each round
     */
    public void takeTurns() {
        configurator.matchTakeTurnURL();
        if (currentPlayer == null) return;
        currentPlayer = (currentPlayer == playerA) ? playerB : playerA;
        roundAction = new RoundAction();
        message = "Now it's " + currentPlayer.getName().substring(1) + "'s turn!";
    }

    /**
     * Check if game has a winner currently 
     */
    public void checkWinner() {
        // win check
        roundAction.getRoundWorker().checkIfWin();
        roundAction.getRoundPossibleMoves();
        roundAction.getRoundPossibleBuilds();


        if (playerA.isWinner()){
            winner =  playerA;

        }
            
        else if (playerB.isWinner()){
            winner =  playerB;
  
        }
        if (winner != null){
           
            isRunning = false;
            configurator.initCells();
            phase = "Start Game";
            message = "Congratulation! " + winner.getName().substring(1) + " is the winner!";
        }
    }


}
