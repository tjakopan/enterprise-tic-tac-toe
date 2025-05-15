package tictactoe.core;

import java.util.Objects;

public abstract sealed class CellState {
  static Played played(final Player player) {
    return new Played(player);
  }

  static Empty empty() {
    return Empty.INSTANCE;
  }

  public static final class Played extends CellState {
    private final Player player;

    private Played(Player player) {
      this.player = player;
    }

    public Player player() {
      return player;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      var that = (Played) obj;
      return Objects.equals(this.player, that.player);
    }

    @Override
    public int hashCode() {
      return Objects.hash(player);
    }

    @Override
    public String toString() {
      return "Played[" + "player=" + player + ']';
    }
  }

  public static final class Empty extends CellState {
    private static final Empty INSTANCE = new Empty();

    private Empty() {}

    @Override
    public boolean equals(Object obj) {
      return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
      return 1;
    }

    @Override
    public String toString() {
      return "Empty[]";
    }
  }
}
