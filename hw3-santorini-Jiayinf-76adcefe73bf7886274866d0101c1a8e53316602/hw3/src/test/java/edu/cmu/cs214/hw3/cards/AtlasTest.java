package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class AtlasTest {
    private God Atlas;
    private God Human;
    private Game game;
    

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("A", "B");
        Atlas = new Atlas();
        Human = new Human();
        game.getCurrentPlayer().setGod(Atlas);
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

        List<Cell> possibleCells = Atlas.getMovableCells(worker, game);

        assertFalse(possibleCells.contains(game.getBoard().getCell(2, 1)));
        assertTrue(possibleCells.contains(game.getBoard().getCell(0, 2)));
        assertFalse(possibleCells.contains(game.getBoard().getCell(2, 3)));
    }

    @Test
    public void testDoBuildWithPower() {
        // Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(1, 2));
        Cell buildOn = game.getBoard().getCell(1,3 );
        
        Atlas.setAns("Yes");
        assertTrue(Atlas.canbuildRandomDome());
        Atlas.doBuild(buildOn);

        assertEquals(buildOn.getHeight(), 4);
        assertTrue(buildOn.isCompleted());
        
    }

    @Test
    public void testDoBuildWithoutPower() {
        // Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(1, 2));
        Cell buildOn = game.getBoard().getCell(1,3 );
        
        Atlas.setAns("No");
        Atlas.doBuild(buildOn);

        assertEquals(buildOn.getHeight(), 1);
        assertFalse(buildOn.isCompleted());
        
    }

}
