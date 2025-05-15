package tictactoe.core;

import static tictactoe.core.HorizontalPosition.*;
import static tictactoe.core.VerticalPosition.*;

import tictactoe.core.CellState.Empty;
import tictactoe.core.CellState.Played;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jspecify.annotations.Nullable;

public final class TicTacToe implements TicTacToeApi {
  private List<List<CellPosition>> linesToCheck() {
    final Function<VerticalPosition, List<CellPosition>> makeHLine =
        v -> Arrays.stream(HorizontalPosition.values()).map(h -> new CellPosition(h, v)).toList();
    final List<List<CellPosition>> hLines =
        Arrays.stream(VerticalPosition.values()).map(makeHLine).toList();

    final Function<HorizontalPosition, List<CellPosition>> makeVLine =
        h -> Arrays.stream(VerticalPosition.values()).map(v -> new CellPosition(h, v)).toList();
    final List<List<CellPosition>> vLines =
        Arrays.stream(HorizontalPosition.values()).map(makeVLine).toList();

    final List<CellPosition> diagonalLine1 =
        List.of(
            new CellPosition(LEFT, TOP),
            new CellPosition(H_CENTER, V_CENTER),
            new CellPosition(RIGHT, BOTTOM));
    final List<CellPosition> diagonalLine2 =
        List.of(
            new CellPosition(LEFT, BOTTOM),
            new CellPosition(H_CENTER, V_CENTER),
            new CellPosition(RIGHT, TOP));

    final List<List<CellPosition>> result = new ArrayList<>();
    result.addAll(hLines);
    result.addAll(vLines);
    result.add(diagonalLine1);
    result.add(diagonalLine2);
    return Collections.unmodifiableList(result);
  }

  private DisplayInfo getDisplayInfo(final GameState gameState) {
    return new DisplayInfo(gameState.cells());
  }

  public Cell getCell(final GameState gameState, final CellPosition posToFind) {
    return gameState.cells().stream()
        .filter(cell -> cell.pos().equals(posToFind))
        .findFirst()
        .orElseThrow();
  }

  private GameState updateCell(final Cell newCell, final GameState gameState) {
    final Function<Cell, Cell> substituteNewCell =
        oldCell -> oldCell.pos().equals(newCell.pos()) ? newCell : oldCell;

    // get a copy of the cells, with the new cell swapped in
    final List<Cell> newCells = gameState.cells().stream().map(substituteNewCell).toList();

    // return a new game state with the new cells
    return new GameState(newCells);
  }

  private boolean isGameWonBy(final Player player, final GameState gameState) {
    // helper to check if a cell was played by a particular player
    final BiPredicate<Player, Cell> cellWasPlayedBy =
        (playerToCompare, cell) ->
            switch (cell.state()) {
              case Empty ignored -> false;
              case Played played -> played.player().equals(playerToCompare);
            };

    // helper to see if every cell in the line has been played
    final BiPredicate<Player, List<CellPosition>> lineIsAllSamePlayer =
        (p, cellPostList) ->
            cellPostList.stream()
                .map(pos -> getCell(gameState, pos))
                .allMatch(cell -> cellWasPlayedBy.test(p, cell));

    return linesToCheck().stream().anyMatch(line -> lineIsAllSamePlayer.test(player, line));
  }

  private boolean isGameTied(final GameState gameState) {
    // helper to check if a cell was played by any player
    final Predicate<Cell> cellWasPlayed =
        cell ->
            switch (cell.state()) {
              case Empty ignored -> false;
              case Played ignored -> true;
            };

    return gameState.cells().stream().allMatch(cellWasPlayed);
  }

  private List<CellPosition> remainingMoves(final GameState gameState) {
    // helper to return player position if a cell is playable
    final Function<Cell, @Nullable CellPosition> playableCell =
        cell ->
            switch (cell.state()) {
              case Empty ignored -> cell.pos();
              case Played ignored -> null;
            };

    //noinspection NullableProblems
    return gameState.cells().stream().map(playableCell).filter(Objects::nonNull).toList();
  }

  private Player otherPlayer(final Player player) {
    return switch (player) {
      case X -> Player.O;
      case O -> Player.X;
    };
  }

  private MoveResult moveResultFor(
      final Player player, final DisplayInfo displayInfo, final List<NextMoveInfo> nextMoves) {
    return switch (player) {
      case X -> MoveResult.playerXToMove(displayInfo, nextMoves);
      case O -> MoveResult.playerOToMove(displayInfo, nextMoves);
    };
  }

  private NextMoveInfo makeNextMoveInfo(
      final Function<Player, Function<CellPosition, Function<GameState, MoveResult>>> f,
      final Player player,
      final GameState gameState,
      final CellPosition cellPos) {
    final MoveCapability capability =
        new MoveCapability(() -> f.apply(player).apply(cellPos).apply(gameState));
    return new NextMoveInfo(cellPos, capability);
  }

  private MoveResult makeMoveResultWithCapabilities(
      final Function<Player, Function<CellPosition, Function<GameState, MoveResult>>> f,
      final Player player,
      final GameState gameState,
      final List<CellPosition> cellPosList) {
    final DisplayInfo displayInfo = getDisplayInfo(gameState);
    final List<NextMoveInfo> nextMoves =
        cellPosList.stream()
            .map(cellPos -> makeNextMoveInfo(f, player, gameState, cellPos))
            .toList();
    return moveResultFor(player, displayInfo, nextMoves);
  }

  private MoveResult playerMove(
      final Player player, final CellPosition cellPos, final GameState gameState) {
    final Cell newCell = new Cell(cellPos, CellState.played(player));
    final GameState newGameState = updateCell(newCell, gameState);
    final DisplayInfo displayInfo = getDisplayInfo(newGameState);

    if (isGameWonBy(player, newGameState)) {
      return MoveResult.gameWon(displayInfo, player);
    } else if (isGameTied(newGameState)) {
      return MoveResult.gameTied(displayInfo);
    } else {
      final Player otherPlayer = otherPlayer(player);
      final List<CellPosition> remainingMoves = remainingMoves(newGameState);
      return makeMoveResultWithCapabilities(
          p -> cp -> gs -> playerMove(p, cp, gs), otherPlayer, newGameState, remainingMoves);
    }
  }

  @Override
  public MoveCapability newGame() {
    final List<CellPosition> allPositions = new ArrayList<>(9);
    for (final HorizontalPosition h : HorizontalPosition.values()) {
      for (final VerticalPosition v : VerticalPosition.values()) {
        allPositions.add(new CellPosition(h, v));
      }
    }

    final List<Cell> emptyCells =
        allPositions.stream().map(pos -> new Cell(pos, CellState.empty())).toList();

    final GameState gameState = new GameState(emptyCells);

    final MoveResult moveResult =
        makeMoveResultWithCapabilities(
            p -> cp -> gs -> playerMove(p, cp, gs), Player.X, gameState, allPositions);

    return new MoveCapability(() -> moveResult);
  }
}
