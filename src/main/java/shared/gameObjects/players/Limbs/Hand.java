package shared.gameObjects.players.Limbs;

import client.main.Settings;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Limb;
import shared.handlers.levelHandler.LevelHandler;

public class Hand extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Hand(Boolean isLeft, GameObject parent, LevelHandler levelHandler) {
    //17 15
    super(-3, 20, 3, 20, 17, 15, ObjectType.Limb, isLeft, parent, 0, 0, levelHandler);
  }


  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    if (isLeft) {
      imageView.setRotate(11);
    } else {
      imageView.setRotate(-15);
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/hand.png");
  }
}
