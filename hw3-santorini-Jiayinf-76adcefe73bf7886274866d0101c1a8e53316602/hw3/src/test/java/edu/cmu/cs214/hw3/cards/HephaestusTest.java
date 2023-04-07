package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Worker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HephaestusTest {
    private God Hephaestus;
    private God Human;
    private Game game;

    @Before
    public void init() throws Exception {
        game = new Game();
        game.initGame("214", "so hard");
        Hephaestus = new Hephaestus();
        Human = new Human();
        game.getCurrentPlayer().setGod(Hephaestus);
        game.getOpponentPlayer().setGod(Human);
        game.pickStartingPosition(new int[]{1, 2});
        game.pickStartingPosition(new int[]{2, 3});
        game.pickStartingPosition(new int[]{3, 2});
        game.pickStartingPosition(new int[]{0, 3});
        game.chooseWorker(new int[] {2, 3});
    }

    @Test
    public void testBuildPowerCells1() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell buildOn = game.getBoard().getCell(2, 4);
        Hephaestus.doBuild(buildOn);
        Hephaestus.setAns("Yes");
        List<Cell> possibleCells = Hephaestus.getBuildableCells(worker, game);
        assertEquals(buildOn.getHeight(), 1);
        assertTrue(possibleCells.contains(buildOn));
        assertEquals(possibleCells.size(),1);
        Hephaestus.doBuild(buildOn);
        assertEquals(buildOn.getHeight(), 2);
    }
    @Test
    public void testBuildPowerDomeCells2() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell buildOn = game.getBoard().getCell(2, 4);
        buildOn.addLevel();
        buildOn.addLevel();
        Hephaestus.doBuild(buildOn);
        assertFalse(Hephaestus.canAdditionalBuild());

        assertEquals(buildOn.getHeight(), 3);
    }


    @Test
    public void testDoBuildInNextRound() {
        Worker worker = game.getCurrentPlayer().getWorkerByPosition(game.getBoard().getCell(2, 3));

        Cell buildOn = game.getBoard().getCell(2, 4);
        Hephaestus.doBuild(buildOn);
        assertEquals(buildOn.getHeight(), 1);
        Hephaestus.setAns("No");
        List<Cell> possibleCells = Hephaestus.getBuildableCells(worker, game);
        assertTrue(possibleCells.contains(buildOn));
        assertFalse(possibleCells.size()==1);

        
        Hephaestus.doBuild(buildOn);
        assertEquals(buildOn.getHeight(), 2);
    }

}