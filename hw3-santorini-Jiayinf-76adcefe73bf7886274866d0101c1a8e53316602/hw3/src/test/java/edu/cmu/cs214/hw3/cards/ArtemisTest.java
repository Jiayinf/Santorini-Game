package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ArtemisTest {
    private God Artemis;
    private God Human;
    private Game game;

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("Tom", "Jerry");
        Artemis = new Artemis();
        Human = new Human();
        game.getCurrentPlayer().setGod(Artemis);
        game.getOpponentPlayer().setGod(Human);
        game.pickStartingPosition(new int[]{1, 2});
        game.pickStartingPosition(new int[]{2, 3});
        game.pickStartingPosition(new int[]{3, 2});
        game.pickStartingPosition(new int[]{0, 3});
        game.chooseWorker(new int[] {2, 3});
    }

    @Test
    public void testGetMovableCells1() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell moveTo = game.getBoard().getCell(2, 4);
        Artemis.doMove(worker,moveTo,game);
        Artemis.setAns("Yes");
        List<Cell> possibleCells = Artemis.getMovableCells(worker, game);

        assertTrue(moveTo.isOccupied());
        assertFalse(possibleCells.contains(game.getBoard().getCell(2, 3)));
    }
    @Test
    public void testGetMovableCells2() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell moveTo = game.getBoard().getCell(2, 4);
        Artemis.doMove(worker,moveTo,game);
        Artemis.setAns("No");
        List<Cell> possibleCells = Artemis.getMovableCells(worker, game);

        assertTrue(moveTo.isOccupied());
        assertTrue(possibleCells.contains(game.getBoard().getCell(2, 3)));
    }


    @Test
    public void testDoMoveTwice() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell moveTo1 = game.getBoard().getCell(2, 4);
        Artemis.doMove(worker,moveTo1,game);
        Artemis.setAns("Yes");
        List<Cell> possibleCells = Artemis.getMovableCells(worker, game);
        Cell moveTo2 = game.getBoard().getCell(3, 4);
        assertTrue(possibleCells.contains(moveTo2));
        Artemis.doMove(worker,moveTo2,game);

        assertTrue(moveTo2.isOccupied());
        assertFalse(moveTo1.isOccupied());
        assertFalse(game.getBoard().getCell(2, 3).isOccupied());
    }

    @Test
    public void testDoMoveInNextRound() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell moveTo1 = game.getBoard().getCell(2, 4);
        Artemis.doMove(worker,moveTo1,game);
        Artemis.setAns("No");

        List<Cell> possibleCells = Artemis.getMovableCells(worker, game);
        
        assertTrue(possibleCells.contains(game.getBoard().getCell(2, 3)));
        
        Cell moveTo2 = game.getBoard().getCell(3, 4);
        Artemis.doMove(worker,moveTo2,game);

        assertTrue(moveTo2.isOccupied());
        assertFalse(moveTo1.isOccupied());
        assertFalse(game.getBoard().getCell(2, 3).isOccupied());
    }
}
