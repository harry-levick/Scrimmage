package shared.gameObjects.animator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;

public class Animator {

  // Helpful variables
  private HashMap<String, ArrayList<Image>> animations;
  private ArrayList<Image> currentAnimation;

  private int intervalSpeed;
  private int tickCounter;
  private int currentAnimationCounter;
  private int currentAnimationSize;

  public Animator() {
    animations = new HashMap<>();
    intervalSpeed = 10;
    tickCounter = this.intervalSpeed;
    currentAnimationCounter = 0;
  }

  public void setSpeed(int s) {
    intervalSpeed = s;
  }

  public void supplyAnimation(String animationName, String... args) {
    ArrayList<Image> images = new ArrayList<>();

     for (String s : args) {
       if(s != null) {
         images.add(new Image(s.replace('/', File.separatorChar).trim()));
       }
       else {
         images.add(new Image(("images/backgrounds/base.png").replace('/', File.separatorChar).trim()));
       }
     }
    
    
    animations.put(animationName, images);
    // Support for the default animation
    if (animationName.equals("default")) {
      currentAnimation = animations.get("default");
      switchDefault();
    }
  }

  public void switchAnimation(String animationName) {
    try {
      ArrayList<Image> getAnimation = this.animations.get(animationName);
      if (!currentAnimation.equals(getAnimation)) {
        currentAnimation = getAnimation;
        currentAnimationSize = currentAnimation.size();
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

    return currentAnimation.get(currentAnimationCounter);
  }
}
