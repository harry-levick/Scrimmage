package shared.gameObjects.background;

import java.util.UUID;

public class Background8 extends Background {

  private final String imagePath = "images/backgrounds/background8.svg";

  public Background8(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 700, 700, false, imagePath);
  }
}
