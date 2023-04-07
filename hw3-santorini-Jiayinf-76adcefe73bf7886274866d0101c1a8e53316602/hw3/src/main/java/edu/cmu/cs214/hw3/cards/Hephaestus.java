package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

public class Hephaestus extends God{
    // Keep a private field to record if it can build a second time
    private Cell oldPosition = null;
    private static final int TOP = 3;
    private boolean isAnsYes = false;
    private boolean isAdditionalBuildDome = false;


    /**
     * Finds the possible cells that worker can build block/dome on. 
     * It will set the only possible build as the same cell as the previous one, if it's the additional round.
     * And check if previous build id top level, then don't do additional part.
     *
     * @param worker Chosen worker
     * @param game Current game
     * @return A list of possible cells
     */
    @Override
    public List<Cell> getBuildableCells(Worker worker, Game game) {
        List<Cell> buildableCells = super.getBuildableCells(worker, game);
        if (!isAnsYes) {
            oldPosition = null;
        }
        if (oldPosition != null) {
            buildableCells = new ArrayList<>();
            buildableCells.add(oldPosition);
        }
        return buildableCells;
    }

    /**
     * Build on the current position
     * @param buildOn Position the worker is going to build on
     */
    @Override
    public void doBuild(Cell buildOn) {
        buildOn.addLevel();
        if (buildOn.getHeight() == TOP && getBuildTime() == 0){
            isAdditionalBuildDome = true;
        } else {
            isAdditionalBuildDome = false;
        }

        setBuildTime(getBuildTime()+1);
        
        oldPosition = buildOn;
        
        isAnsYes = false;
    }

    

    /**
     * Check if this god has the power to do an additional build
     * @return
     */
    @Override
    public boolean canAdditionalBuild() {
        if (isAdditionalBuildDome == true){
            return false;
        }


        return true;
    }

    @Override
    public void setAns(String ans) {
        this.isAnsYes = ans.equals("Yes");
        
        // Can only make choice once

    }
    

}
