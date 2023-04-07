package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

import java.util.List;


public class Artemis extends God{
    private Cell oldPosition = null;
    private boolean isAnsYes = false;

    /**
     * Finds the possible cells that worker can build block/dome on. It will remove the last build position
     * as a worker cannot build on the same position twice in a turn.
     *
     * @param worker Chosen worker
     * @param game Current game
     * @return A list of possible cells
     */
    @Override
    public List<Cell> getMovableCells(Worker worker, Game game) {
        List<Cell> movableCells = super.getBuildableCells(worker, game);
        if (!isAnsYes) {
            oldPosition = null;
        }
        if (oldPosition != null) {
            movableCells.remove(oldPosition);
        }
        return movableCells;
    }

    /**
     * move to the new position
     * @param moveTo Position the worker is going to move to
     */
    @Override
    public void doMove(Worker worker, Cell moveTo, Game game) {
        oldPosition = worker.getCurPosition();
        worker.setCurPosition(moveTo);
        isAnsYes = false;
    }


    /**
     * Check if this god has the power to do an additional move
     * @return
     */
    @Override
    public boolean canAdditionalMove() {
        return true;
    }

    @Override
    public void setAns(String ans) {
        this.isAnsYes = ans.equals("Yes");
        // Can only make choice once

    }
}
