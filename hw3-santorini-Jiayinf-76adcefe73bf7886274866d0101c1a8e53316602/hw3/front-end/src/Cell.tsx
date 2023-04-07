import React from 'react';
import { Cell } from './game';

interface Props {
  cell: Cell
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const color = this.props.cell.cssClass;
    return (
      <div className={`cell ${color}`}>{this.props.cell.text}</div>
    )
  }
}

export default BoardCell;