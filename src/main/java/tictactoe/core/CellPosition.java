package tictactoe.core;

import java.util.Objects;

public final class CellPosition {
  private final HorizontalPosition horizontalPosition;
  private final VerticalPosition verticalPosition;

  CellPosition(HorizontalPosition horizontalPosition, VerticalPosition verticalPosition) {
    this.horizontalPosition = horizontalPosition;
    this.verticalPosition = verticalPosition;
  }

  public HorizontalPosition horizontalPosition() {
    return horizontalPosition;
  }

  public VerticalPosition verticalPosition() {
    return verticalPosition;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (CellPosition) obj;
    return Objects.equals(this.horizontalPosition, that.horizontalPosition)
        && Objects.equals(this.verticalPosition, that.verticalPosition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(horizontalPosition, verticalPosition);
  }

  @Override
  public String toString() {
    return "CellPosition["
        + "horizontalPosition="
        + horizontalPosition
        + ", "
        + "verticalPosition="
        + verticalPosition
        + ']';
  }
}
