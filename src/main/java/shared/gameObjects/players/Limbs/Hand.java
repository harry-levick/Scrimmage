package shared.gameObjects.players.Limbs;

import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import shared.gameObjects.Destructable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;

/**
 * Hand limb of a player
 */
public class Hand extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Hand(Boolean isLeft, Limb armRight, Player player, LevelHandler levelHandler, UUID uuid) {
    //17 15
    super(-3, 20, 3, 20, 17, 15, ObjectType.Limb, isLeft, armRight, player, 0, 0, levelHandler,
        uuid);
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
  public void deductHp(int damage, GameObject source) {
    ((Destructable) parent).deductHp(damage, source);
  }


  @Override
  protected void rotateAnimate() {

  }
}
