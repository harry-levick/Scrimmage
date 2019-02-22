package shared.gameObjects.background;

import java.util.UUID;

public class Background7 extends Background {

  private final String imagePath = "images/backgrounds/background7.svg";

  public Background7(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 700, 700, false, imagePath);
  }
}
