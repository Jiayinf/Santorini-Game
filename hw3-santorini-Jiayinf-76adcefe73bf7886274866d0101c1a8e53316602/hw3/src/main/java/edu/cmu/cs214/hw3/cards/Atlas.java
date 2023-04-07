package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Cell;

public class Atlas extends God{
    private boolean isAnsYes = false;
    private static final int DOM = 4;

    /**
     * Build on the current position, check if build a dome or not
     * @param buildOn Position the worker is going to build on
     */

    @Override
    public void doBuild(Cell buildOn) {
        if (!isAnsYes){
            buildOn.addLevel();
        } else {
            while (buildOn.getHeight()<DOM){
                buildOn.addLevel();
            }
        } 
        isAnsYes = false; 
    }


    /**
     * Check if this god has the power to do a random level dome
     * @return
     */

    @Override
    public boolean canbuildRandomDome() {
        return true;
    }

    /**
     * Check the user's response
     * @return
     */

    @Override
    public void setAns(String ans) {
        this.isAnsYes = ans.equals("Yes");
    }
}
