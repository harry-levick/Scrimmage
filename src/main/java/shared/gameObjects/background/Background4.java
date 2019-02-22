package shared.gameObjects.background;

import java.util.UUID;

public class Background4 extends Background {

  private final String imagePath = "images/backgrounds/background4.svg";

  public Background4(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 700, 700, false, imagePath);
  }
}
