package shared.gameObjects.players.Limbs;

import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;

/**
 * Body limb of a player
 */
public class Body extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Body(Player parent, LevelHandler levelHandler) {
    super(0, 0, 22, 64, 39, 31, ObjectType.Limb, false, parent, parent, 0, 0, levelHandler);
    limbMaxHealth = 99999;
    limbHealth = limbMaxHealth;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/body_front.png");
  }

  @Override
  protected void rotateAnimate() {
    // TODO Auto-generated method stub

  }
}

