package tictactoe.core;

import java.util.List;

record GameState(List<Cell> cells) {
  GameState(final List<Cell> cells) {
    this.cells = List.copyOf(cells);
  }
}
