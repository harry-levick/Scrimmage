package shared.gameObjects.background;

import java.util.UUID;

public class Background6 extends Background {

  private final String imagePath = "images/backgrounds/background6.svg";

  public Background6(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 700, 700, false, imagePath);
  }
}
