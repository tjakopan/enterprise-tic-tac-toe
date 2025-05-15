package tictactoe.core;

import java.util.List;
import java.util.function.UnaryOperator;
import tictactoe.core.MoveResult.GameTied;
import tictactoe.core.MoveResult.GameWon;
import tictactoe.core.MoveResult.PlayerOToMove;
import tictactoe.core.MoveResult.PlayerXToMove;

public final class TicTacToeWithLogging implements TicTacToeApi {
  private final TicTacToeApi api;

  public TicTacToeWithLogging(final TicTacToeApi api) {
    this.api = api;
  }

  private MoveCapability transformCapability(
      final UnaryOperator<MoveResult> transformMr,
      final Player player,
      final CellPosition cellPos,
      final MoveCapability cap) {
    return new MoveCapability(
        () -> {
          System.out.printf("LOGINFO: %s played %s%n", player, cellPos);
          final MoveResult moveResult = cap.get();
          return transformMr.apply(moveResult);
        });
  }

  private NextMoveInfo transformNextMove(
      final UnaryOperator<MoveResult> transformMr, final Player player, final NextMoveInfo move) {
    final CellPosition cellPos = move.posToPlay();
    final MoveCapability cap = move.capability();
    return new NextMoveInfo(cellPos, transformCapability(transformMr, player, cellPos, cap));
  }

  private MoveResult transformMoveResult(final MoveResult moveResult) {
    return switch (moveResult) {
      case GameTied ignored -> {
        System.out.println("LOGINFO: Game tied");
        yield moveResult;
      }
      case GameWon gameWon -> {
        System.out.printf("LOGINFO: Game won by %s%n", gameWon.player());
        yield moveResult;
      }
      case PlayerOToMove playerOToMove -> {
        final List<NextMoveInfo> nextMoves =
            playerOToMove.validMoves().stream()
                .map(move -> transformNextMove(this::transformMoveResult, Player.O, move))
                .toList();
        yield MoveResult.playerOToMove(playerOToMove.displayInfo(), nextMoves);
      }
      case PlayerXToMove playerXToMove -> {
        final List<NextMoveInfo> nextMoves =
            playerXToMove.validMoves().stream()
                .map(move -> transformNextMove(this::transformMoveResult, Player.X, move))
                .toList();
        yield MoveResult.playerXToMove(playerXToMove.displayInfo(), nextMoves);
      }
    };
  }

  @Override
  public MoveCapability newGame() {
    return new MoveCapability(() -> transformMoveResult(api.newGame().get()));
  }
}
