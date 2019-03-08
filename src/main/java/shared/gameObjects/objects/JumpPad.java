package shared.gameObjects.objects;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class JumpPad extends GameObject {

  public JumpPad(double x, double y, UUID uuid) {
    super(x, y, 2, 1, ObjectType.Bot, uuid);
  }
  @Override
  public void initialiseAnimation() {

  }
}
