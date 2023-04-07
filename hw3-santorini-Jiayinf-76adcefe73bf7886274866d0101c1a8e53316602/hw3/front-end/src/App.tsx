import React from 'react';
import './App.css'; // import the css file to enable your styles.

import { GameState, Cell} from './game';
import BoardCell from './Cell';


/**
 * Define the type of the props field for a React component
 */
interface Props { }

/**
 * Using generics to specify the type of props and state.
 * props and state is a special field in a React component.
 * React will keep track of the value of props and state.
 * Any time there's a change to their values, React will
 * automatically update (not fully re-render) the HTML needed.
 * 
 * props and state are similar in the sense that they manage
 * the data of this component. A change to their values will
 * cause the view (HTML) to change accordingly.
 * 
 * Usually, props is passed and changed by the parent component;
 * state is the internal value of the component and managed by
 * the component itself.
 */
// var oldHref = "http://localhost:3000";

class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;
  private godnames: string[] = [];
  private additionalBuildControl: string = "No";
  private additionalMoveControl: string = "No";
  private domeControl: string = "";


  /**
   * @param props has type Props
   */
  constructor(props: Props) {
    super(props)
    /**
     * state has type GameState as specified in the class inheritance.
     */
    
    this.state = {
      message: " ",
      cells: [
        { text: " ", x :0, y: 0, isOccupied: false, cssClass: ""},
        { text: " ", x :1, y: 0, isOccupied: false, cssClass: ""},
        { text: " ", x :2, y: 0, isOccupied: false, cssClass: ""},
        { text: " ", x :3, y: 0, isOccupied: false, cssClass: ""},
        { text: " ", x :4, y: 0, isOccupied: false, cssClass: ""},
        { text: " ", x :0, y: 1, isOccupied: false, cssClass: ""},
        { text: " ", x :1, y: 1, isOccupied: false, cssClass: ""},
        { text: " ", x :2, y: 1, isOccupied: false, cssClass: ""},
        { text: " ", x :3, y: 1, isOccupied: false, cssClass: ""},
        { text: " ", x :4, y: 1, isOccupied: false, cssClass: ""},
        { text: " ", x :0, y: 2, isOccupied: false, cssClass: ""},
        { text: " ", x :1, y: 2, isOccupied: false, cssClass: ""},
        { text: " ", x :2, y: 2, isOccupied: false, cssClass: ""},
        { text: " ", x :3, y: 2, isOccupied: false, cssClass: ""},
        { text: " ", x :4, y: 2, isOccupied: false, cssClass: ""},
        { text: " ", x :0, y: 3, isOccupied: false, cssClass: ""},
        { text: " ", x :1, y: 3, isOccupied: false, cssClass: ""},
        { text: " ", x :2, y: 3, isOccupied: false, cssClass: ""},
        { text: " ", x :3, y: 3, isOccupied: false, cssClass: ""},
        { text: " ", x :4, y: 3, isOccupied: false, cssClass: ""},
        { text: " ", x :0, y: 4, isOccupied: false, cssClass: ""},
        { text: " ", x :1, y: 4, isOccupied: false, cssClass: ""},
        { text: " ", x :2, y: 4, isOccupied: false, cssClass: ""},
        { text: " ", x :3, y: 4, isOccupied: false, cssClass: ""},
        { text: " ", x :4, y: 4, isOccupied: false, cssClass: ""},
      ],
      winner: null,
      phase: " ",}
  }
  // hook react
    play(x: number, y: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      if (this.state.phase === "running"){
        const response = await fetch(`/pickingStartingPosition?x=${x}&y=${y}`)
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"]);

      } else 
      if (this.state.phase === "Choose Worker"){

        //initialize some special controls
        this.additionalBuildControl = "No";
        this.additionalMoveControl = "No";
        this.domeControl = "No";
        
        const response = await fetch(`/round?x=${x}&y=${y}`)
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"]);

      } 
        // for additional move state
        else if (this.state.phase === "Set Additional Move"){
        var again = this.additionalMoveControl;
        const response = await fetch(`/round/move?again=${again}&x=${x}&y=${y}`);
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"])
  
      } else if (this.state.phase === "Set Move"){
        const response = await fetch(`/round/move?x=${x}&y=${y}`)
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"]);

      } 
        // for additional build state
        else if (this.state.phase === "Set Additional Build"){
        var again = this.additionalBuildControl;
        const response = await fetch(`/round/build?again=${again}&x=${x}&y=${y}`);
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"])
  
      } 
        // for build dome state
        else if (this.state.phase === "Set special dome Build"){
        var dome = this.domeControl;
        const response = await fetch(`/round/build?dome=${dome}&x=${x}&y=${y}`);
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"])

      }else if (this.state.phase === "Set Builds"){
        
        const response = await fetch(`/round/build?x=${x}&y=${y}`)
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"]);

      } 

      else if (this.state.phase === "start game"){
        const response = await fetch('/initGame');
        console.log (response);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"]);
      }
      
    }
  }

  /**
   * This function will call when top control bar is used (I.E. having additional god power that requires user's response)
   * 
   **/
 
  moreAction (x: number){
    return async() => {
      if (this.state.phase === "Set Additional Move"){
        if (x === 1){
          this.additionalMoveControl = "Yes";
          console.log (this.additionalMoveControl);
        } else {
          this.additionalMoveControl = "No";
          console.log (this.additionalMoveControl);
        }
      } else if (this.state.phase === "Set Additional Build"){
        if (x === 1){
          this.additionalBuildControl = "Yes";
          console.log (this.additionalBuildControl);
        } else {
          this.additionalBuildControl = "No";
          console.log (this.additionalBuildControl);
        }
      } else if (this.state.phase === "Set special dome Build"){
        if (x === 1){
          this.domeControl = "Yes";
          console.log (this.domeControl);
        } else {
          this.domeControl = "No";
          console.log (this.domeControl);
        }
      }
    }
  }



  newGame = async () => {
    const response = await fetch('/initGame');
    const json = await response.json();
    console.log (json);
    this.setState(json["GameState"]);
  }


  undo = async () => {
    const response = await fetch('/undo');
    const json = await response.json();
    this.setState(json);
  }

  chooseGod(god: string) {
    return async () => {
      if (this.godnames.length < 1){
        this.godnames.push(god);
      } else if (this.godnames.length === 1){
        this.godnames.push(god);
        var godA = this.godnames[0];
        var godB = this.godnames[1]
        const response = await fetch(`/chooseGod?godA=${godA}&godB=${godB}`);
        const json = await response.json();
        console.log (json);
        this.setState(json["GameState"]);
      }
      
    }
  } 



  createCell(cell: Cell, index: number): React.ReactNode {
    // if (!cell.isOccupied) {
      /**
       * key is used for React when given a list of items. It
       * helps React to keep track of the list items and decide
       * which list item need to be updated.
       * @see https://reactjs.org/docs/lists-and-keys.html#keys
       */

      //console.log("create cell after click the cell", this.state.cells)
      return (
        <div key={index}>
          <a href='/' onClick={this.play(cell.x, cell.y)}>
            <BoardCell cell={cell}></BoardCell>
          </a>
        </div>
      )
    
  }

  

  /**
   * This function will call after the HTML is rendered.
   * We update the initial state by creating a new game.
   * @see https://reactjs.org/docs/react-component.html#componentdidmount
   */
  componentDidMount(): void {
    /**
     * setState in DidMount() will cause it to render twice which may cause
     * this function to be invoked twice. Use initialized to avoid that.
     */
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

 

  /**
   * The only method you must define in a React.Component subclass.
   * @returns the React element via JSX.
   * @see https://reactjs.org/docs/react-component.html
   */
  render(): React.ReactNode {
    /**
     * We use JSX to define the template. An advantage of JSX is that you
     * can treat HTML elements as code.
     * @see https://reactjs.org/docs/introducing-jsx.html
     */
    
      return (
      <div>
        <div id="instructions">{this.state.message}</div>
        
        <div id= "topbar">
          <button onClick={/* get the function, not call the function */this.moreAction(1)}>Yes</button>
          <button onClick={/* get the function, not call the function */this.moreAction(0)}>No</button>
          
        </div>
        <div id="board">
          {this.state.cells.map((cell, i) => this.createCell(cell, i))}
        </div>
        <div id="bottombar">
          <button onClick={/* get the function, not call the function */this.newGame}>New Game</button>

          <button onClick={/* get the function, not call the function */this.chooseGod("Apollo")}>Apollo</button>
          <button onClick={/* get the function, not call the function */this.chooseGod("Artemis")}>Artemis</button> 
          <button onClick={/* get the function, not call the function */this.chooseGod("Athena")}>Athena</button>
          <button onClick={/* get the function, not call the function */this.chooseGod("Atlas")}>Atlas</button> 
          <button onClick={/* get the function, not call the function */this.chooseGod("Demeter")}>Demeter</button>
          <button onClick={/* get the function, not call the function */this.chooseGod("Hephaestus")}>Hephaestus</button>
          <button onClick={/* get the function, not call the function */this.chooseGod("Minotaur")}>Minotaur</button>
          <button onClick={/* get the function, not call the function */this.chooseGod("Pan")}>Pan</button> 
          
          <button onClick={/* get the function, not call the function */this.chooseGod("Human")}>Human</button> 
          {/* Exercise: implement Undo function */}
          <button onClick={/* get the function, not call the function */this.undo}>Undo</button>
        </div>
      </div>
    );
    
  }
}

export default App;
