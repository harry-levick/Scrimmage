package shared.gameObjects.players.Limbs;

import client.main.Settings;
import javafx.scene.Group;
import shared.gameObjects.Destructable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import sun.security.krb5.internal.crypto.Des;

/**
 * Hand limb of a player
 */
public class Hand extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Hand(Boolean isLeft, Limb armRight, Player player, LevelHandler levelHandler) {
    //17 15
    super(-3, 20, 3, 20, 17, 15, ObjectType.Limb, isLeft, armRight, player, 0, 0, levelHandler);
  }


  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    if (isLeft) {
      imageView.setRotate(15);
    } else {
      imageView.setRotate(-15);
    }
  }

  @Override
  public void initialiseAnimation() {
    //this.animation.supplyAnimation("default", "images/player/skin0/hand.png");
    this.animation.supplyAnimation("default", "images/blank.png");
  }

  @Override
  public void deductHp(int damage) {
    ((Destructable) parent).deductHp(damage);
  }


  @Override
  protected void rotateAnimate() {
    // TODO Auto-generated method stub

  }
}
