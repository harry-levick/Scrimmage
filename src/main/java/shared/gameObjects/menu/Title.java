package shared.gameObjects.menu;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class Title extends GameObject {

  public Title(double x, double y, double sizeX, double sizeY, UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectType.Button, uuid);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/title.png");
  }

}
