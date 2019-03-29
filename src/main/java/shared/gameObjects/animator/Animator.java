package shared.gameObjects.animator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;

/**
 * Handles multi-frame sprite animations
 */
public class Animator {

  // Helpful variables
  private HashMap<String, ArrayList<Image>> animations;
  private ArrayList<Image> currentAnimation;
  private String currentAnimationName;

  private int intervalSpeed;
  private int tickCounter;
  private int currentAnimationCounter;
  private int currentAnimationSize;

  /**
   * Constructs animator
   */
  public Animator() {
    animations = new HashMap<>();
    intervalSpeed = 10;
    tickCounter = this.intervalSpeed;
    currentAnimationCounter = 0;
    try {
      supplyAnimation("default", "images/blank.png");
    } catch (Exception e) {

    }

  }

  public void setSpeed(int s) {
    intervalSpeed = s;
  }

  /**
   * Adds an animation composed of frames from a list of files
   *
   * @param animationName Name of the animation to switch into
   * @param args List of files
   */
  public void supplyAnimation(String animationName, String... args) {
    ArrayList<Image> images = new ArrayList<>();

    for (String s : args) {
      if (s != null) {
        images.add(new Image(s.replace('/', File.separatorChar).trim()));
      } else {
        images.add(
            new Image(("images/backgrounds/base.png").replace('/', File.separatorChar).trim()));
      }
    }

    animations.put(animationName, images);
    // Support for the default animation
    if (animationName.equals("default")) {
      currentAnimation = animations.get("default");
      currentAnimationName = "default";
      switchDefault();
    }
  }

  /**
   * This method scales the image to the given width and height
   *
   * @param animationName The name of the animation
   * @param h Height of the image after scaling
   * @param w Width of the image after scaling
   * @param ratio True if ratio is preserved
   * @param paths Multiple paths to images
   */
  public void supplyAnimationWithSize(
      String animationName, double h, double w, boolean ratio, String... paths) {
    ArrayList<Image> images = new ArrayList<>();
    for (String path : paths) {
      images.add(new Image(path.replace('/', File.separatorChar).trim(), w, h, ratio, true));
    }
    animations.put(animationName, images);
    // Support for the default animation
    if (animationName.equals("default")) {
      currentAnimation = animations.get("default");
      switchDefault();
    }
  }

  /**
   * Changes the current animation state to a new one
   *
   * @param animationName New animationt state to switch into
   */
  public void switchAnimation(String animationName) {
    try {
      ArrayList<Image> getAnimation = this.animations.get(animationName);
      if (!currentAnimation.equals(getAnimation)) {
        currentAnimation = getAnimation;
        currentAnimationSize = currentAnimation.size();
        currentAnimationCounter = 0;
        tickCounter = intervalSpeed;
        currentAnimationName = animationName;
      }
    } catch (Exception e) {

    }
  }

  /**
   * Switches to default animation state
   */
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

  public String getName() {
    return currentAnimationName;
  }
}
