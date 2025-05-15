package tictactoe.core;

import java.util.List;
import java.util.Objects;

public abstract sealed class MoveResult {
  static PlayerXToMove playerXToMove(
      final DisplayInfo displayInfo, final List<NextMoveInfo> validMoves) {
    return new PlayerXToMove(displayInfo, validMoves);
  }

  static PlayerOToMove playerOToMove(
      final DisplayInfo displayInfo, final List<NextMoveInfo> validMoves) {
    return new PlayerOToMove(displayInfo, validMoves);
  }

  static GameWon gameWon(final DisplayInfo displayInfo, final Player player) {
    return new GameWon(displayInfo, player);
  }

  static GameTied gameTied(final DisplayInfo displayInfo) {
    return new GameTied(displayInfo);
  }

  public static final class PlayerXToMove extends MoveResult {
    private final DisplayInfo displayInfo;
    private final List<NextMoveInfo> validMoves;

    private PlayerXToMove(final DisplayInfo displayInfo, final List<NextMoveInfo> validMoves) {
      this.displayInfo = displayInfo;
      this.validMoves = List.copyOf(validMoves);
    }

    public DisplayInfo displayInfo() {
      return displayInfo;
    }

    public List<NextMoveInfo> validMoves() {
      return validMoves;
    }

    @Override
    public boolean equals(final Object o) {
      if (!(o instanceof final PlayerXToMove that)) return false;
      return Objects.equals(displayInfo, that.displayInfo)
          && Objects.equals(validMoves, that.validMoves);
    }

    @Override
    public int hashCode() {
      return Objects.hash(displayInfo, validMoves);
    }

    @Override
    public String toString() {
      return "PlayerXToMove{" + "displayInfo=" + displayInfo + ", validMoves=" + validMoves + '}';
    }
  }

  public static final class PlayerOToMove extends MoveResult {
    private final DisplayInfo displayInfo;
    private final List<NextMoveInfo> validMoves;

    private PlayerOToMove(final DisplayInfo displayInfo, final List<NextMoveInfo> validMoves) {
      this.displayInfo = displayInfo;
      this.validMoves = List.copyOf(validMoves);
    }

    public DisplayInfo displayInfo() {
      return displayInfo;
    }

    public List<NextMoveInfo> validMoves() {
      return validMoves;
    }

    @Override
    public boolean equals(final Object o) {
      if (!(o instanceof final PlayerXToMove that)) return false;
      return Objects.equals(displayInfo, that.displayInfo)
          && Objects.equals(validMoves, that.validMoves);
    }

    @Override
    public int hashCode() {
      return Objects.hash(displayInfo, validMoves);
    }

    @Override
    public String toString() {
      return "PlayerXToMove{" + "displayInfo=" + displayInfo + ", validMoves=" + validMoves + '}';
    }
  }

  public static final class GameWon extends MoveResult {
    private final DisplayInfo displayInfo;
    private final Player player;

    private GameWon(final DisplayInfo displayInfo, final Player player) {
      this.displayInfo = displayInfo;
      this.player = player;
    }

    public DisplayInfo displayInfo() {
      return displayInfo;
    }

    public Player player() {
      return player;
    }

    @Override
    public boolean equals(final Object o) {
      if (!(o instanceof final GameWon gameWon)) return false;
      return Objects.equals(displayInfo, gameWon.displayInfo) && player == gameWon.player;
    }

    @Override
    public int hashCode() {
      return Objects.hash(displayInfo, player);
    }

    @Override
    public String toString() {
      return "GameWon{" + "displayInfo=" + displayInfo + ", player=" + player + '}';
    }
  }

  public static final class GameTied extends MoveResult {
    private final DisplayInfo displayInfo;

    private GameTied(final DisplayInfo displayInfo) {
      this.displayInfo = displayInfo;
    }

    public DisplayInfo displayInfo() {
      return displayInfo;
    }

    @Override
    public boolean equals(final Object o) {
      if (!(o instanceof final GameTied gameTied)) return false;
      return Objects.equals(displayInfo, gameTied.displayInfo);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(displayInfo);
    }

    @Override
    public String toString() {
      return "GameTied{" + "displayInfo=" + displayInfo + '}';
    }
  }
}
