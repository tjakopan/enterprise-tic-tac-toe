package tictactoe.core;

import java.util.Objects;

public final class Cell {
  private final CellPosition pos;
  private final CellState state;

  Cell(final CellPosition pos, final CellState state) {
    this.pos = pos;
    this.state = state;
  }

  public CellPosition pos() {
    return pos;
  }

  public CellState state() {
    return state;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Cell) obj;
    return Objects.equals(this.pos, that.pos) && Objects.equals(this.state, that.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pos, state);
  }

  @Override
  public String toString() {
    return "Cell[" + "pos=" + pos + ", " + "state=" + state + ']';
  }
}
