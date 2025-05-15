package tictactoe.core;

import java.util.List;
import java.util.Objects;

public final class DisplayInfo {
  private final List<Cell> cells;

  DisplayInfo(final List<Cell> cells) {
    this.cells = List.copyOf(cells);
  }

  public List<Cell> cells() {
    return cells;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (DisplayInfo) obj;
    return Objects.equals(this.cells, that.cells);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cells);
  }

  @Override
  public String toString() {
    return "DisplayInfo[" + "cells=" + cells + ']';
  }
}
