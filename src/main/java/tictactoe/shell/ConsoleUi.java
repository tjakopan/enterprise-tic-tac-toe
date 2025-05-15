package tictactoe.shell;

import static tictactoe.core.VerticalPosition.*;

import java.io.*;
import java.util.List;
import org.jspecify.annotations.Nullable;
import tictactoe.core.*;
import tictactoe.core.MoveResult.GameTied;
import tictactoe.core.MoveResult.GameWon;
import tictactoe.core.MoveResult.PlayerOToMove;
import tictactoe.core.MoveResult.PlayerXToMove;

public final class ConsoleUi {
  private ConsoleUi() {}

  static void gameLoop(final TicTacToeApi api) {
    System.out.println("\n------------------------------\n");

    MoveResult moveResult = api.newGame().get();
    do {
      //noinspection IfCanBeSwitch
      if (moveResult instanceof GameTied gameTied) {
        displayCells(gameTied.displayInfo());
        System.out.println("GAME OVER - Tie");
        System.out.println();
        final boolean playAgain = askToPlayAgain();
        if (playAgain) {
          moveResult = api.newGame().get();
        } else {
          break;
        }
      } else if (moveResult instanceof GameWon gameWon) {
        displayCells(gameWon.displayInfo());
        System.out.printf("GAME WON by %s%n", gameWon.player());
        System.out.println();
        final boolean playAgain = askToPlayAgain();
        if (playAgain) {
          moveResult = api.newGame().get();
        } else {
          break;
        }
      } else if (moveResult instanceof PlayerOToMove playerOToMove) {
        displayCells(playerOToMove.displayInfo());
        System.out.println("Player O to move");
        displayNextMoves(playerOToMove.validMoves());
        System.out.println("Enter an int corresponding to a displayed move or q to quit:");
        final String inputStr = readLine();
        if ("q".equals(inputStr)) {
          break;
        } else {
          try {
            final int inputIndex = Integer.parseInt(inputStr);

            final MoveCapability capability = getCapability(inputIndex, playerOToMove.validMoves());
            if (capability != null) {
              moveResult = capability.get();
            } else {
              System.out.printf("...No move found for input index %d. Try again.%n", inputIndex);
            }
          } catch (NumberFormatException e) {
            System.out.println("...Please enter an int corresponding to a displayed move.");
          }
        }
      } else if (moveResult instanceof PlayerXToMove playerXToMove) {
        displayCells(playerXToMove.displayInfo());
        System.out.println("Player X to move");
        displayNextMoves(playerXToMove.validMoves());
        System.out.println("Enter an int corresponding to a displayed move or q to quit:");
        final String inputStr = readLine();
        if ("q".equals(inputStr)) {
          break;
        } else {
          try {
            final int inputIndex = Integer.parseInt(inputStr);

            final MoveCapability capability = getCapability(inputIndex, playerXToMove.validMoves());
            if (capability != null) {
              moveResult = capability.get();
            } else {
              System.out.printf("...No move found for input index %d. Try again.%n", inputIndex);
            }
          } catch (NumberFormatException e) {
            System.out.println("...Please enter an int corresponding to a displayed move.");
          }
        }
      }
    } while (true);
  }

  static void displayCells(final DisplayInfo displayInfo) {
    final List<Cell> cells = displayInfo.cells();
    final List<Cell> topCells =
        cells.stream().filter(cell -> cell.pos().verticalPosition() == TOP).toList();
    final List<Cell> centerCells =
        cells.stream().filter(cell -> cell.pos().verticalPosition() == V_CENTER).toList();
    final List<Cell> bottomCells =
        cells.stream().filter(cell -> cell.pos().verticalPosition() == BOTTOM).toList();

    printCells(topCells);
    printCells(centerCells);
    printCells(bottomCells);
    System.out.println();
  }

  private static String cellToStr(final Cell cell) {
    return switch (cell.state()) {
      case CellState.Empty ignored -> "-";
      case CellState.Played played ->
          switch (played.player()) {
            case X -> "X";
            case O -> "O";
          };
    };
  }

  private static void printCells(final List<Cell> cells) {
    cells.stream()
        .map(ConsoleUi::cellToStr)
        .reduce((s1, s2) -> s1 + "|" + s2)
        .ifPresent(s -> System.out.printf("|%s|%n", s));
  }

  private static @Nullable MoveCapability getCapability(
      final int selectedIndex, final List<NextMoveInfo> nextMoves) {
    if (selectedIndex < nextMoves.size()) {
      return nextMoves.get(selectedIndex).capability();
    } else {
      return null;
    }
  }

  private static boolean askToPlayAgain() {
    System.out.println("Would you like to play again (y/n)?");
    return switch (readLine()) {
      case "y" -> true;
      case "n" -> false;
      default -> askToPlayAgain();
    };
  }

  private static String readLine() {
    final Console console = System.console();
    if (console != null) {
      return console.readLine();
    }

    final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      return reader.readLine();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void displayNextMoves(final List<NextMoveInfo> nextMoves) {
    for (int i = 0; i < nextMoves.size(); i++) {
      System.out.printf("%d) %s%n", i, nextMoves.get(i).posToPlay());
    }
  }

  static void startGame(final TicTacToeApi api) {
    gameLoop(api);
  }
}
