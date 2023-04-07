package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

public class Pan extends God {

    /**
     * move to the new position for both current worker and opponent worker
     * check if this move goes down two level, if so, win!
     * @param moveTo Position the worker is going to move to
     * @param worker the worker is going to move
     * @param game current game
     */
    public void doMove(Worker worker, Cell moveTo, Game game) {
        Cell oldPosition = worker.getCurPosition();
        super.doMove(worker, moveTo, game);
        if (oldPosition.getHeight() - moveTo.getHeight() >= 2 ) {
            worker.getPlayer().setIsWinner();
        }
    }
}
