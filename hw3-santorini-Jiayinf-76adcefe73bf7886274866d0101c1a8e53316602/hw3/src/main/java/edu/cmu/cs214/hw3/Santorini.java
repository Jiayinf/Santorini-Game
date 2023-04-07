package edu.cmu.cs214.hw3;


import com.google.gson.Gson;

import edu.cmu.cs214.hw3.models.Game;

import edu.cmu.cs214.hw3.utils.GameState;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;

import java.util.HashMap;

import java.util.Map;




// import com.google.gson.Gson;

/**
 * The URLs in this game are set as following:
 * 1) "/initGame?nameA=&nameB=" => initGame(nameA, nameB)
 * 2) "/chooseGod?godA=&godB=" => chooseGod(godA, godB)
 * 3) "/pickStartingPosition?x=&y=" => x, y -> int[] pos => pickStartingPosition(pos)
 * 4) "/round?x=&y=" => x, y -> int[] curPos => chooseWorker(curPos) -> computeMovableCells() (check if size == 0, if yes then redirect to choose another one)
 * 5) "/round/move?x=&y=" => x, y -> int[] movePos => roundMove(movePos) -> if fail (choose another cell, otherwise continue)
 *  => call getWinner(), if not -> computeBuildableCells() (check if size == 0, lose)
 * 7) "/round/build?x=&y=" => x, y -> int[] buildPos => roundBuild(buildPos) if fail (redirect to another cell), otherwise
 *  => check if canAdditionalBuild() -> no, takeTurns() => yes render UI
 *     7.1) "?again" => yes -> 7.2); no -> takeTurns()
 */
public final class Santorini extends NanoHTTPD {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            new Santorini();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private Game game;


    public Santorini() throws IOException {
        super(PORT);

        this.game = new Game();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    private int[] parsePos(String x, String y) {
        return new int[]{Integer.parseInt(x), Integer.parseInt(y)};
    }

    @Override
    public Response serve(IHTTPSession session) {
        Gson gson = new Gson();

            
        Map<String,Object> map = new HashMap<>();
    

        try {
            String uri = session.getUri();
            Map<String, String> params = session.getParms();
            this.game.setMessage("");
            if (uri.equals("/initGame")) {
                this.game = new Game();
                
                this.game.initGame("PlayerA", "PlayerB");   
                
            }
            else if (uri.equals("/chooseGod")) {
                this.game.chooseGod(params.get("godA"), params.get("godB"));
                
            }
            else if (uri.equals("/pickingStartingPosition")) {
                int[] pos = parsePos(params.get("x"), params.get("y"));
                this.game.pickStartingPosition(pos);
                
            }
            else if (uri.equals("/round")) {
                int[] curPos = parsePos(params.get("x"), params.get("y"));
                this.game.chooseWorker(curPos);
                if (this.game.getRoundAction().getRoundWorker() != null) {
                    this.game.computeMovableCells();
                }
                
            }
            else if (uri.equals("/round/move")) {
                //for additional move round
                if (params.get("again") != null) {
                    // If worker do not want to do an additional build
                    if(params.get("again").equals("No")) {
                        this.game.getCurrentPlayer().getGod().setAns(params.get("again"));
                        this.game.computeBuildableCells();
                        this.game.setPhase("Set Builds");
                    } else {
                        // If worker want to do an additional move
                        this.game.getCurrentPlayer().getGod().setAns(params.get("again"));
                        this.game.computeMovableCells();
                        int[] moveTo = parsePos(params.get("x"), params.get("y"));
                        boolean success = this.game.roundMove(moveTo);
                        
                        if (success) {
                            this.game.checkWinner();
                            if (this.game.getWinner() == null) {
                                this.game.computeBuildableCells();
                                this.game.checkWinner();
                                this.game.setPhase("Set Builds");
                            }
                        } 
                    }
                           
                //for normal move round       
                } else {
                    int[] moveTo = parsePos(params.get("x"), params.get("y"));
                    boolean success = this.game.roundMove(moveTo);
                    if (success) {
                        boolean canAdditionalMove = this.game.getCurrentPlayer().getGod().canAdditionalMove();   
                        if (canAdditionalMove) {
                            this.game.setMessage("Move Again?");
                            this.game.setPhase("Set Additional Move");
                        } else {
                            this.game.checkWinner();
                            if (this.game.getWinner() == null) {
                                this.game.computeBuildableCells();
                                this.game.checkWinner();
                                this.game.setPhase("Set Builds");
                            }
                        }
                    }
                }
            }
            
            
            else if (uri.equals("/round/build")) {

                //for additional build round
                if (params.get("again") != null) {
                    // If worker do not want to do an additional build
                    if(params.get("again").equals("No")) {
                        this.game.getCurrentPlayer().getGod().setAns(params.get("again"));
                        this.game.getCurrentPlayer().getGod().setBuildTime(0);
                        this.game.takeTurns();
                        this.game.setPhase("Choose Worker");
                    } else {
                        // If worker want to do an additional build
                        this.game.getCurrentPlayer().getGod().setAns(params.get("again"));
                        this.game.computeBuildableCells();
                        int[] buildOn = parsePos(params.get("x"), params.get("y"));
                        boolean success = this.game.roundBuild(buildOn);
                        
                        if (success) {
                            this.game.getCurrentPlayer().getGod().setBuildTime(0);
                            this.game.takeTurns();
                            this.game.setPhase("Choose Worker");
                        } 
                    }
                           
                        
                } else {
                    int[] buildOn = parsePos(params.get("x"), params.get("y"));
                    //for god who can build a dome at random level
                    boolean canbuildRandomDome = this.game.getCurrentPlayer().getGod().canbuildRandomDome();
                    if (canbuildRandomDome){
                        this.game.setMessage("Build Dome?");
                        this.game.setPhase("Set special dome Build");
                        
                        if (params.get("dome") != null) {
            
                                    this.game.getCurrentPlayer().getGod().setAns(params.get("dome"));
                                    this.game.computeBuildableCells();
                                    boolean success = this.game.roundBuild(buildOn);
                                    if (success) {
                                        this.game.takeTurns();
                                        this.game.setPhase("Choose Worker");
                                    } 
                                       
                                    
                            }
                        
                    // for nornal build round  
                    } else {
                        buildOn = parsePos(params.get("x"), params.get("y"));
                        boolean success = this.game.roundBuild(buildOn);
                        // check if worker can do additional build
                        if (success) {
                            boolean canAdditionalBuild = this.game.getCurrentPlayer().getGod().canAdditionalBuild();
                            
                            if (canAdditionalBuild) {
                            this.game.setMessage("Build Again?");
                            this.game.setPhase("Set Additional Build");
                            } else {
                                this.game.getCurrentPlayer().getGod().setBuildTime(0);
                                this.game.takeTurns();
                                this.game.setPhase("Choose Worker");
                            }
                        }
                    }
                    
                }

            } 





            GameState status = GameState.forGame(this.game);
            

            
            
            map.put("GameState", status); 
            String json = gson.toJson(map);

            return newFixedLengthResponse(json);



            


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}