package shared.gameObjects.players;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public abstract class Limb extends GameObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public Limb(double x, double y, double sizeX, double sizeY,
      ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
  }

  public abstract void initialiseAnimation();

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
  }
}

