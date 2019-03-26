package shared.gameObjects.menu.main.account.lootbox;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class Lootbox extends GameObject {

  public Lootbox(double x, double y, double sizeX, double sizeY, UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectType.Button, uuid);
  }
}
