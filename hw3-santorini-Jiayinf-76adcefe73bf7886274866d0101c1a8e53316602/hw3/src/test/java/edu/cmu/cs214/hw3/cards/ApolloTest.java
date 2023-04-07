package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class ApolloTest {

    private God Apollo;
    private God Human;
    private Game game;
    

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("freya", "yoyo");
        Apollo = new Apollo();
        Human = new Human();
        game.getCurrentPlayer().setGod(Apollo);
        game.getOpponentPlayer().setGod(Human);
        game.pickStartingPosition(new int[]{1, 2});
        game.pickStartingPosition(new int[]{2, 3});
        game.pickStartingPosition(new int[]{2, 1});
        game.pickStartingPosition(new int[]{0, 3});
        game.chooseWorker(new int[] {1, 2});
    }

    @Test
    public void testGetMovableCells() {
        Cell curPos = game.getBoard().getCell(1, 2);
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(curPos);

        List<Cell> possibleCells = Apollo.getMovableCells(worker, game);

        assertTrue(possibleCells.contains(game.getBoard().getCell(2, 1)));
        assertTrue(possibleCells.contains(game.getBoard().getCell(0, 3)));
        assertFalse(possibleCells.contains(game.getBoard().getCell(2, 3)));
    }

    @Test
    public void testDoMoveWithPower() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(1, 2));
        Cell moveTo = game.getBoard().getCell(2, 1);
        Worker opponentWorker = game.getOpponentPlayer().getWorkerByPosition(moveTo);

        Apollo.doMove(worker, moveTo, game);

        assertEquals(worker.getCurPosition(), moveTo);
        assertEquals(opponentWorker.getCurPosition(), game.getBoard().getCell(1, 2));
    }

}
