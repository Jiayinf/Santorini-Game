package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.ArrayList;
import java.util.List;

public class Human extends God{

    public List<Cell> getMovableCells(Worker worker, Game game) {

        List<Cell> movableCells = new ArrayList<>();

        Cell workerPosition = worker.getCurPosition();
        List<Cell> neighbors = game.getBoard().getNeighbors(workerPosition);
        

        for(Cell cell : neighbors) {
            if (!cell.isOccupied() && cell.isClimbable(workerPosition)) {
                movableCells.add(cell);
            }
        }
        // System.out.println(movableCells.size());
        return movableCells;
    }

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

    public void doMove(Worker worker, Cell move, Game game) {
        worker.setCurPosition(move);
    }

    public void doBuild(Cell build) {
        build.addLevel();
    }

}
