package edu.cmu.cs214.hw3.models;

public class Cell {
    private final int x;
    private final int y;
    private boolean isOccupied;
    private int height;
    private static final int TOP = 3;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int DOM = 4;
    //private boolean preferable = false;
    private String text = "";
    private String link;
    private String cssClass = "";


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isOccupied = false;
        this.height = 0;
        this.cssClass = " ";
        this.text = " ";

    }


    @Override
    public String toString() {
        return "{ \"text\": \"" + this.getText() + "\"," +
                " \"playable\": " + !this.isOccupied + "," +
                " \"x\": " + this.x + "," +
                " \"y\": " + this.y + "," +
                " \"cssclass\": " + this.getCssClass() + " }" ;
    }


    /**
     * Check if a cell is occupied.
     * A cell is considered as occupied either has a worker or a completed tower.
     *
     * @return True if cell is not occupied, false otherwise.
     */
    public Boolean isOccupied() { return isOccupied || isCompleted(); }


    public void setOccupied() {
        isOccupied = true;

    }

    public void setFree() {
        isOccupied = false;
 
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public static int getTop() { return TOP; }

    public boolean isEqual(Cell that) {
        if (this == that) return true;
        return this.getX() == that.getX()
                && this.getY() == that.getY();
    }

    public int getHeight() {
        return height;
    }

    /**
     * Increase height to the current cell, whose height can only up to 4,
     * if it reaches 3, then the tower is capped with a dome and is considered
     * as completed. Completed tower cannot be added more levels.
     */
    public void addLevel() {
        if(height <= TOP) {
            height += 1;
        }
        if (height == ONE){
            this.cssClass = "orange";
        }
        else if (height == TWO){
            this.cssClass = "green";
        }
        else if (height == TOP){
            this.cssClass = "yellow";
        } 
        else if (height == DOM){
            this.cssClass = "pink";
        }
    }

    /**
     * Check if a cell is completed.
     * Completed cell cannot increase its height anymore.
     *
     * @return True if cell is completed.
     */
    public boolean isCompleted() { return height == TOP + 1; }

    /**
     * This functions checks if worker can successfully climb up to
     * this tower from his current position.
     *
     * The worker can only move up a maximum of one level higher to the cell that is not completed.
     * @param from worker's current position
     * @return True if worker can climb up to the tower; False otherwise.
     */
    public boolean isClimbable(Cell from) {
        if(isCompleted()) return false;
        return height - from.getHeight() <= 1;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setText(String avatar) {
        this.text = avatar;
    }
    public void setCssClass(String css) {
        this.cssClass = css;
    }

    public String getLink() {return this.link;}

    public String getText() 
    {return this.text;}

    public String getCssClass() {return this.cssClass;}

    public void clearStyle() {
        this.text = "";
        this.cssClass = "";
    }
}
