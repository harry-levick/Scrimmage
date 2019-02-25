package shared.gameObjects.background;

import java.util.UUID;

public class Background1 extends Background {

  private final String imagePath = "images/backgrounds/background1.svg";

  public Background1(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 1080, 1920, false, imagePath);
  }
}
