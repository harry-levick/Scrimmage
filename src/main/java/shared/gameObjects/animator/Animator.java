package shared.gameObjects.animator;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class Animator {

  // Helpful variables
  ArrayList<Image> images;
  int numberOfImages;
  int currentImage = 0;

  int animationInterval;
  int animationTimer;

  public void Animator(ArrayList<String> imagePaths, int animationInterval) {
    this.images = generateImages(imagePaths);
    this.currentImage = currentImage;
    this.numberOfImages = this.images.size();

    this.animationInterval = animationInterval;
    this.animationTimer = 0;
  }

  private ArrayList<Image> generateImages(ArrayList<String> paths) {
    return null;
  }

  public void update() {
    // This called every frame.
    this.animationTimer += 1;
    if (this.animationTimer > this.animationInterval) {
      this.animationTimer = 0;
      this.currentImage += 1;
      if (this.currentImage > this.numberOfImages) {
        this.currentImage = 0;
      }
    }
  }

  public void render(double X, double Y) {
    // draw(images.get(currentImage),x,y)
  }
}
