package tictactoe.core;

import java.util.List;
import java.util.Objects;

public final class GameState {
  private final List<Cell> cells;

  GameState(final List<Cell> cells) {
    this.cells = List.copyOf(cells);
  }

  List<Cell> cells() {
    return cells;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (GameState) obj;
    return Objects.equals(this.cells, that.cells);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cells);
  }

  @Override
  public String toString() {
    return "GameState[" + "cells=" + cells + ']';
  }
}
