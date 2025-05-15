package tictactoe.shell;

import tictactoe.core.TicTacToe;
import tictactoe.core.TicTacToeWithLogging;

public class ConsoleApplication {
  public static void main(String[] args) {
    // final var api = new TicTacToe();
    final var api = new TicTacToeWithLogging(new TicTacToe());
    ConsoleUi.startGame(api);
  }
}
