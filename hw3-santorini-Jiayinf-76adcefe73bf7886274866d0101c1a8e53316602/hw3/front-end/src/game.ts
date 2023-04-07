interface GameState {
  message: string;
  cells: Cell[];
  phase: String;
  winner: number | null;
}

interface Cell {
  
  text: string;
  isOccupied: boolean;
  x: number;
  y: number;
  cssClass: string;
  
}
export type { GameState, Cell }