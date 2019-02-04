package shared.gameObjects.animator;

import javafx.scene.image.Image;

import java.util.HashMap;

public class Animator {

  // Helpful variables
  private HashMap<String, Image[]> animations;
  private Image[] currentAnimation;

  private int intervalSpeed;
  private int tickCounter;
  private int currentAnimationCounter;
  private int currentAnimationSize;

  public Animator() {
    animations = new HashMap<String, Image[]>();
    intervalSpeed = 10;
    tickCounter = this.intervalSpeed;
    currentAnimationCounter = 0;
  }

  public void setSpeed(int s) {
    intervalSpeed = s;
  }

  public void supplyAnimation(String animationName, Image[] images) {
    animations.put(animationName, images);
    // Support for the default animation
    if (animationName.equals("default")) {
      currentAnimation = animations.get("default");
      switchDefault();
    }
  }

  public void switchAnimation(String animationName) {
    try {
      Image[] getAnimation = this.animations.get(animationName);
      if (!currentAnimation.equals(getAnimation)) {
        currentAnimation = getAnimation;
        currentAnimationSize = currentAnimation.length;
        currentAnimationCounter = 0;
        tickCounter = intervalSpeed;
      }
    } catch (Exception e) {
      System.out.println("No animation of name " + animationName + " exists. Exepction:" + e);
    }
  }

  public void switchDefault() {
    switchAnimation("default");
  }

  public void update() {
    // This is called every frame.
    tickCounter -= 1;
    if (tickCounter <= 0) {
      tickCounter = intervalSpeed;
      currentAnimationCounter += 1;
      if (currentAnimationCounter >= currentAnimationSize) {
        currentAnimationCounter = 0;
      }
    }
  }

  public Image getImage() {

    return currentAnimation[currentAnimationCounter];
  }
}
