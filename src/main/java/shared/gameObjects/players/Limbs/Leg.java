package shared.gameObjects.players.Limbs;

import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Behaviour;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;

/**
 * Leg limb of a player
 */
public class Leg extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Leg(Boolean isLeft, Player parent, LevelHandler levelHandler) {
    super(19, 87, 43, 87, 21, 23, ObjectType.Limb, isLeft, parent, parent, 0, 0, levelHandler);
    limbMaxHealth = player.getHealth() / 4;
    limbHealth = limbMaxHealth;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/skin" + settings.getData().getActiveSkin()[3] + "/leg.png");
  }

  @Override
  public void updateSkinRender(int id) {
    this.animation.supplyAnimation("default", "images/player/skin" + id + "/leg.png");
  }


  private void walkAnimation() {

    int interval = 7;
    int segments = 3;
    int localTime = this.player.getAnimationTimer() % (interval * segments);

    // Control to switch the leg animations depending on movement direction.
    boolean control = isLeft;
    int inverse = 1;
    if(this.behaviour == Behaviour.WALK_LEFT) {
      control =!control;
      inverse = -1;
    }

    if(localTime < interval*1) {
      if(control) {
        imageView.setRotate(45*inverse);
      }
      else {
        imageView.setRotate(-45*inverse);
      }
    }
    else if(localTime < interval*2) {
      if(control) {
        imageView.setRotate(-40*inverse);
      }
      else {
        imageView.setRotate(0*inverse);
      }
    }
  }

  private void jumpAnimation() {

    // Control to switch the leg animations depending on movement direction.
    boolean control = isLeft;
    int inverse = 1;
    if(this.behaviour == Behaviour.WALK_LEFT) {
      control =!control;
      inverse = -1;
    }

    if(control) {
      imageView.setRotate(45*inverse);
    }

  }


  @Override
  protected void rotateAnimate() {
    if(this.behaviour == Behaviour.JUMP || this.player.getJumped()) {
      jumpAnimation();
    }

    else if(this.behaviour == Behaviour.WALK_LEFT || this.behaviour == Behaviour.WALK_RIGHT) {
      walkAnimation();
    }
  }



}
