package tictactoe.core;

import java.util.Objects;
import java.util.function.Supplier;

public final class MoveCapability implements Supplier<MoveResult> {
  private final Supplier<MoveResult> value;

  MoveCapability(final Supplier<MoveResult> value) {
    this.value = value;
  }

  public Supplier<MoveResult> value() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (MoveCapability) obj;
    return Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "MoveCapability[" + "value=" + value + ']';
  }

  @Override
  public MoveResult get() {
    return value.get();
  }
}
