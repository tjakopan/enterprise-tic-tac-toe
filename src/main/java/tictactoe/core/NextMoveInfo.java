package tictactoe.core;

import java.util.Objects;

public final class NextMoveInfo {
  private final CellPosition posToPlay;
  private final MoveCapability capability;

  NextMoveInfo(CellPosition posToPlay, MoveCapability capability) {
    this.posToPlay = posToPlay;
    this.capability = capability;
  }

  public CellPosition posToPlay() {
    return posToPlay;
  }

  public MoveCapability capability() {
    return capability;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (NextMoveInfo) obj;
    return Objects.equals(this.posToPlay, that.posToPlay)
        && Objects.equals(this.capability, that.capability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(posToPlay, capability);
  }

  @Override
  public String toString() {
    return "NextMoveInfo[" + "posToPlay=" + posToPlay + ", " + "capability=" + capability + ']';
  }
}
