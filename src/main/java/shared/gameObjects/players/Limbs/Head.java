package shared.gameObjects.players.Limbs;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;

public class Head extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Head(Boolean isLeft,
      Player parent) {
    super(0, 0, 0, 0, 48, 58, ObjectID.Player, isLeft, parent);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/head.png");
  }
}
