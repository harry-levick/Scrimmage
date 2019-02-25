package shared.gameObjects.players.Limbs;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;

public class Leg extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Leg(Boolean isLeft, Player parent) {
    super(19, 87, 43, 87, 21, 23, ObjectID.Player, isLeft, parent);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/leg.png");
  }
}
