package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;

public class Apollo extends God {

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


    @Override
    public List<Cell> getMovableCells(Worker worker, Game game) {
        Cell workerPos = worker.getCurPosition();
        List<Cell> neighbors = game.getBoard().getNeighbors(workerPos);
        List<Cell> movableCells = super.getMovableCells(worker, game);

        for(Cell cell : neighbors) {
            if (cell.isOccupied() && !cell.isCompleted() ) {
                if (game.getOpponentPlayer().getWorkerByPosition(cell) != null) {
                    // Cell opponentNewPos = workerPos;
                    // if (opponentNewPos != null && !opponentNewPos.isOccupied()) {
                        movableCells.add(cell);
                    // }
                }
            }
        }
        return movableCells;
    }

    /**
     * Move the current worker to the selected cell, set opponent to current worker position
     *
     * @param worker Chosen worker
     * @param moveTo Position the worker is going to move to
     * @param game Current game
     */
    @Override
    public void doMove(Worker worker, Cell moveTo, Game game) {
        Cell workerPos = worker.getCurPosition();
        System.out.println(workerPos.getX());
        System.out.println(workerPos.getY());
        Worker opponentWorker = game.getOpponentPlayer().getWorkerByPosition(moveTo);
        if (opponentWorker != null) {
            moveTo.setFree();
            super.doMove(worker, moveTo, game);
            Cell opponentNewPos = workerPos;
            opponentWorker.setCurPosition(opponentNewPos);
        } else {
            super.doMove(worker, moveTo, game);
        }
        
    }

    
}

