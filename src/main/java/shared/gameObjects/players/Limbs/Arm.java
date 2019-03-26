package shared.gameObjects.players.Limbs;

import client.main.Settings;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Behaviour;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;

/**
 * The Arm object of a player
 */
public class Arm extends Limb {


  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Arm(Boolean isLeft, Player parent, LevelHandler levelHandler) {
    super(13, 62, 53, 62, 17, 33, ObjectType.Limb, isLeft, parent, parent, 0, 0, levelHandler);
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

  @Override
  public void addChild(GameObject child) {
    super.addChild(child);
    settings.getLevelHandler().getLimbs().put(child.getUUID(), (Limb) child);
  }

  /**
   * Remove the image from the imageView by setting the image to null
   */
  @Override
  public void removeRender() {
    if (imageView != null) {
      imageView.setImage(null);
      Platform.runLater(
          () -> {
            root.getChildren().remove(imageView);
          }
      );
    }
    children.forEach(child -> child.removeRender());
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
