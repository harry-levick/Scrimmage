package shared.gameObjects.background;

import java.util.UUID;

public class Background2 extends Background {

  private final String imagePath = "images/backgrounds/background2.svg";

  public Background2(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 700, 700, false, imagePath);
  }
}
