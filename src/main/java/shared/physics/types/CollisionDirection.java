package shared.physics.types;

import shared.util.maths.Vector2;

public enum CollisionDirection {
  UP,
  DOWN,
  LEFT,
  RIGHT;

  public static CollisionDirection getDirection(Vector2 noramlizedVector) {
    float angle = noramlizedVector.angle();
    if (45 > angle && angle > -45) {
      if (noramlizedVector.getY() >= 0) return UP;
      else return DOWN;
    } else {
      if (noramlizedVector.getX() <= 0) return RIGHT;
      else return LEFT;
    }
  }
}
