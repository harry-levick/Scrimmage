package shared.physics.types;

public enum ColliderLayer {
  DEFAULT(0),
  PLAYER(1),
  OBJECT(2),
  WALL(3);

  int id;

  ColliderLayer(int i) {
    id = i;
  }

  public int toInt() {
    return id;
  }
}
