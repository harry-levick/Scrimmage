package shared.gameObjects.players.Limbs;

import client.main.Settings;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Behaviour;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Punch;
import shared.handlers.levelHandler.LevelHandler;
import shared.util.maths.Vector2;

/**
 * The Arm object of a player
 */
public class Arm extends Limb {

  private static double AIM_ANGLE_MAX = 110;
  private static double DEFAULT_ANGLE_LEFT = 6;
  private static double DEFAULT_ANGLE_RIGHT = -9;
  private static double PI = 3.141592654;
  private double angleRadian;
  private Limb hand;
  private Player playerParent;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Arm(Boolean isLeft, Player parent, LevelHandler levelHandler) {
    super(13, 62, 53, 62, 17, 33, ObjectType.Limb, isLeft, parent, parent, 0, 0, levelHandler);
    this.playerParent = parent;
    rotate = new Rotate();
    rotate.setPivotX(10);
    rotate.setPivotY(10);
    limbMaxHealth = player.getHealth() / 2;
    limbHealth = limbMaxHealth;
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    rotate = new Rotate();
    rotate.setPivotX(10);
    rotate.setPivotY(10);
  }

  @Override
  public void destroy() {
    super.destroy();
    children.forEach(object -> object.destroy());
  }

  public void renderabc() {
    if (this.hand == null) {
      return;
    }

    imageView.getTransforms().clear();

    double xHand = this.getX();
    double yHand = this.getY();
    double xMouse = this.playerParent.mouseX;
    double yMouse = this.playerParent.mouseY;
    /* 2 situations:
     *    i. With player have holding and holding not Punch
     *       - True when the mouse pointer is pointing to the left
     *       - False otherwise
     *   ii. With player have holding (a proper gun / melee)
     *       - True when it is aiming to the left
     *       - False otherwise
     */
    boolean toTheLeft =
        (playerParent.getHolding() == null || playerParent.getHolding() instanceof Punch) ?
            playerParent.isPointingLeft()
            : playerParent.isAimingLeft();

    Vector2 mouseSubHand = new Vector2(xMouse - xHand, yMouse - yHand);
    angleRadian = mouseSubHand.angleBetween(Vector2.Zero());
    double angle = angleRadian * 180 / PI;

    // Change hand when aiming the other way
    double angleHorizontal; // angle wrt horizontal axis
    if (toTheLeft) {
      angleHorizontal = (mouseSubHand.angleBetween(Vector2.Left())) * 180 / PI; // degree
      if (angleHorizontal > AIM_ANGLE_MAX) {
        angle = angleHorizontal - 180f;
      }
      if (angleHorizontal > 90f) {
        angle = angleHorizontal * (yMouse > this.getY() ? -1 : 1);
      }
    } else { // playerParent pointing Right
      angleHorizontal = (mouseSubHand.angleBetween(Vector2.Right())) * 180 / PI; // degree
      if (angleHorizontal > AIM_ANGLE_MAX) {
        angle = 180f - angleHorizontal;
      }
      if (angleHorizontal > 90f) {
        angle = angleHorizontal * (yMouse > this.getY() ? 1 : -1);
      }
    }

    if (isLeft) {
      if (toTheLeft) {
        rotate.setAngle(270 - angle);
      } else {
        rotate.setAngle(DEFAULT_ANGLE_LEFT);
      }
    } else { // is right arm
      if (!toTheLeft) {
        rotate.setAngle(270 + angle);
      } else {
        rotate.setAngle(DEFAULT_ANGLE_RIGHT);
      }
    }

    imageView.getTransforms().add(rotate);

    super.render();
  }

  @Override
  public void addChild(GameObject child) {
    super.addChild(child);
    settings.getLevelHandler().getLimbs().put(child.getUUID(), (Limb) child);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/skin" + settings.getData().getActiveSkin()[2] + "/arm.png");
  }

  @Override
  public void updateSkinRender(int id) {
    this.animation.supplyAnimation("default", "images/player/skin" + id + "/arm.png");
  }

  private void jumpAnimation() {
    // Control to switch the leg animations depending on movement direction.
    boolean control = isLeft;
    int inverse = 1;
    if(this.behaviour == Behaviour.WALK_LEFT) {
      control =!control;
      inverse = -1;
    }
    
    if(!control) {
      imageView.setRotate(-130*inverse);
    }
    
  }
  
  private void walkAnimation() {
  
    int interval = 7;
    int segments = 3;
    int localTime = this.player.getAnimationTimer() % (interval * segments);
    
    boolean control = isLeft;
    int inverse = 1;
    if(this.behaviour == Behaviour.WALK_LEFT) {
      control =!control;
      inverse = -1;
    }
    
    // Default is for running left.
    if(this.behaviour == Behaviour.WALK_LEFT || this.behaviour == Behaviour.WALK_RIGHT) {   
      
      if(localTime < interval * 1) {
        if(control) {
          imageView.setRotate(60*inverse);
        }
        else {
          imageView.setRotate(-100*inverse);
        }
      }
      
      else if(localTime < interval * 2) {
        if(control) {
          imageView.setRotate(-40*inverse);
        }
        else {
          imageView.setRotate(-50*inverse);
        }
      }
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
