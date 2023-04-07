package edu.cmu.cs214.hw3.utils;

import edu.cmu.cs214.hw3.models.Board;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Player;

// import java.util.Arrays;

/**
 * This file updates the game status for rendering game in the web page. It only works for the implementation
 * of GUI, and does not relate to anything of the domain core design.
 */
public final class GameState{

    private String message = " ";
    private final Cell[] cells;
    private String phase = " ";
    private String winner;
    private static final int COL = 5;
    private static final int ROW = 5;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int TOP = 3;
    private static final int DOM = 4;
    // private GameState[] history;
  
    // private final String turn;

    private GameState(String message, Cell[] cells, String phase, String winner) {
        this.message = message;
        this.cells = cells;
        this.phase = phase;
        this.winner = winner;
        // this.history = history;
        // this.turn = turn;
    }

    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game);
        String message = getMessage(game);
        String phase = getPhase(game);
        String winner;
        if (game.getWinner() == null){
            winner = "";
        } else {
            winner = game.getWinner().getName();
        }

        return new GameState(message, cells, phase, winner);
    }

    public String getMessage() {
        return this.message;
    }
    public String getWinner() {
        return this.winner;
    }

    public Cell[] getCells() {
        return this.cells;
    }

    public String getPhase() {return this.phase;}

    // @Override
    // public String toString() {
    //     if (this.winner == null){
    //         return ("{ \"message\": \"" + this.message + "\"," +
    //             " \"cells\": " + Arrays.deepToString(this.cells) + "," +
    //             " \"phase\": " + this.phase + "," +
    //             " \"winner\": " + null + " }" );
    //     } else {
    //         return ("{ \"message\": \"" + this.message + "\"," +
    //         " \"cells\": " + Arrays.deepToString(this.cells) + "," +
    //         " \"phase\": " + this.phase + "," +
    //         " \"winner\": " + this.winner.getName() + " }" );
    //     }
    // }

    // @Override
    // public String toString() {
    //     if (this.winner == null) {
    //         return """
    //                 {   "message": %s,
    //                     "cells": %s,
    //                     "phase": %s,
    //                     "winner": null
    //                 }
    //                 """.formatted(this.message, Arrays.deepToString(this.cells), this.phase);
    //     } else {
    //         return """
    //                 {
    //                     "message": %s,
    //                     "cells": %s,
    //                     "phase": %s,
    //                     "winner": %s
    //                 }
    //                """.formatted(this.message, Arrays.deepToString(this.cells), this.phase, this.winner.getName());
    //     }
    // }

    private static String getMessage(Game game) {
        return game.getMessage();
    }

    private static String getPhase(Game game) {
        return game.getPhase();
    }

    private static Cell[] getCells(Game game) {
        Board board = game.getBoard();
        Cell[][] cells = board.getAllCells();
        Cell[] result = new Cell[ROW*COL];
        if(game.getCurrentPlayer() != null) {
            Player playerA = game.getCurrentPlayer().getName().charAt(0) == 'A'
                    ? game.getCurrentPlayer() : game.getOpponentPlayer();
            Player playerB = game.getCurrentPlayer().getName().charAt(0) == 'B'
                    ? game.getCurrentPlayer() : game.getOpponentPlayer();

            for (int x = 0; x < cells.length; x++) {
                for (int y = 0; y < cells[0].length; y++) {
            
                    Cell cell = board.getCell(x, y);
                    
                    // First, clear the previous style
                    cell.clearStyle();
                    // Second, check each cell new style
                    if (playerA.getWorkerByPosition(cell) != null) {
                        cell.setText("X");

                    } else if (playerB.getWorkerByPosition(cell) != null) {
                        cell.setText("O");
 
                    } else {
                        cell.setText("");
                    }
                    int height = cell.getHeight();
                    switch (height) {
                        case ONE: 
                            cell.setCssClass("orange"); 
                            break;
                        case TWO: 
                            cell.setCssClass("green"); 
                            break;
                        case TOP: 
                            cell.setCssClass("yellow"); 
                            break;
                        case DOM: 
                            cell.setCssClass("pink"); 
                            break;
                        default: break;
                    }
                    result[ROW * y + x] = cell;
                }
            }    
        }
        return result;
        // return cells;

        
    }

    // private static Cell[] getCells(Game game) {
    //     Board board = game.getBoard();
    //     Cell[][] cells = board.getAllCells();
    //     Cell result[] = new Cell[25];
        
 
    //     if(game.getCurrentPlayer() != null) {
    //         Player playerA = game.getCurrentPlayer().getName().charAt(0) == 'A'
    //                 ? game.getCurrentPlayer() : game.getOpponentPlayer();
    //         Player playerB = game.getCurrentPlayer().getName().charAt(0) == 'B'
    //                 ? game.getCurrentPlayer() : game.getOpponentPlayer();

            // for (int x = 0; x < cells.length; x++) {
            //     for (int y = 0; y < cells[0].length; y++) {
            //         String text = "";
            //         String cssclass = "";
            //         boolean playable = false;
            //         Cell cell = board.getCell(x, y);
                    
            //         // First, clear the previous style
            //         cell.clearStyle();
            //         // Second, check each cell new style
            //         if (playerA.getWorkerByPosition(cell) != null) {
            //             cell.setAvatar("X");
            //             text = "X";
            //         } else if (playerB.getWorkerByPosition(cell) != null) {
            //             cell.setAvatar("O");
            //             text = "O";
            //         } else {
            //             playable = true;
            //         }
            //         int height = cell.getHeight();
            //         switch (height) {
            //             case 1: 
            //                 cell.setCssClass("orange"); 
            //                 cssclass = "orange";
            //                 break;
            //             case 2: 
            //                 cell.setCssClass("green"); 
            //                 cssclass = "green";
            //                 break;
            //             case 3: 
            //                 cell.setCssClass("yellow"); 
            //                 cssclass = "yellow";
            //                 break;
            //             case 4: 
            //                 cell.setCssClass("pink"); 
            //                 cssclass = "pink";
            //                 break;
            //             default: break;
            //         }
            //         result[5 * y + x] = cell;
    //             }
    //         }
            
    //     }

    //     return result;
    // }

    

}

