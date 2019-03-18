package shared.gameObjects.players.Limbs;

import client.main.Settings;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Behaviour;
import shared.gameObjects.players.Limb;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;

public class Arm extends Limb {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   */
  public Arm(Boolean isLeft, Player parent, LevelHandler levelHandler) {
    super(13, 62, 53, 62, 17, 33, ObjectType.Limb, isLeft, parent, parent, 0, 0, levelHandler);
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    if (isLeft) {
      //imageView.setRotate(6);
    } else {
      //imageView.setRotate(-9);
    }
  }

  @Override
  public void addChild(GameObject child) {
    children.add(child);
    levelHandler.addGameObject(child);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/Standard_Male/arm.png");
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
    
    // Default is for running letft.
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
