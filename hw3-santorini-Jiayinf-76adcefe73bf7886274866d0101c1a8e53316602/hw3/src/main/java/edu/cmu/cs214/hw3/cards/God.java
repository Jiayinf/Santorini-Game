package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.ArrayList;
import java.util.List;

public abstract class God {
    private boolean powerToOpponent= false;
    private int buildTime = 0;

    // set the power to opponent
    public void setUsePowerToOpponent() {
        powerToOpponent = true;
    }

    // give up the power to opponent
    public void setNotUsePowerToOpponent() {
        powerToOpponent = false;
    }

    public boolean ifUsePowerToOpponent() {
        return powerToOpponent;
    }
    
    // check if it's the additional build round
    public void setBuildTime(int num) {
        this.buildTime = num;
    }

    // check if it's the additional build round  
    public int getBuildTime(){
        return this.buildTime;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can move to.
     *
     * A cell is unoccupied, or it's level is not 2 or more levels higher than worker's
     * level is considered as movable.
     * @param worker Chosen worker
     * @param game Current game
     * @return The list of possible cells that worker can move to.
     */

    //computable movable cells, different god will have different power that might affect this
    public List<Cell> getMovableCells(Worker worker, Game game) {
        Cell workerPos = worker.getCurPosition();
        List<Cell> neighbors = game.getBoard().getNeighbors(workerPos);
        List<Cell> movableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied() && cell.isClimbable(workerPos) ) {
                movableCells.add(cell);
            }
        }
        return movableCells;
    }

    /**
     * This function checks the eight neighbors of the worker's current position and
     * finds the possible cells that worker can build block/dome on.
     *
     * A cell is unoccupied is considered as buildable.
     * @param worker Chosen worker
     * @param game Current game
     * @return The list of possible cells that worker can build on.
     */

    //computable buildable cells, different god will have different power that might affect this
    public List<Cell> getBuildableCells(Worker worker, Game game) {
        List<Cell> neighbors = game.getBoard().getNeighbors(worker.getCurPosition());
        List<Cell> buildableCells = new ArrayList<>();

        for(Cell cell : neighbors) {
            if (!cell.isOccupied()) {
                buildableCells.add(cell);
            }
        }
        return buildableCells;
    }


    /**
     * Move the current worker to the selected cell.
     *
     * @param worker Chosen worker
     * @param moveTo Position the worker is going to move to
     * @param game Current game
     */
    public void doMove(Worker worker, Cell moveTo, Game game) {
        worker.setCurPosition(moveTo);
    }

    /**
     * Build blocks/dome on the selected cell.
     *
     * @param buildOn Position the worker is going to build on
     */
    public void doBuild(Cell buildOn) {
        buildOn.addLevel();
    }

    // for god who could build twice
    public boolean canAdditionalBuild() {
        return false;
    }
 
    // for god who could move twice
    public boolean canAdditionalMove() {
        return false;
    }
    
    // for god who could build dome at random level
    public boolean canbuildRandomDome() {
        return false;
    }


    public List<Cell> applyOpponentPowerToMove(List<Cell> movableCells, Worker worker) {
        return movableCells;
    }

    public List<Cell> applyOpponentPowerToBuild(List<Cell> buildableCells) {
        return buildableCells;
    }
    
    // get the answer whether to do additional build/move or not
    public void setAns(String ans) {};
}
