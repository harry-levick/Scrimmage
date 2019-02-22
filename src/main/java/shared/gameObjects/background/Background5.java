package shared.gameObjects.background;

import java.util.UUID;

public class Background5 extends Background {

  private final String imagePath = "images/backgrounds/background5.svg";

  public Background5(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 700, 700, false, imagePath);
  }
}
