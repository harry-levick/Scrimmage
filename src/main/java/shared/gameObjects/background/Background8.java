package shared.gameObjects.background;

import java.util.UUID;

public class Background8 extends Background {

  private static final String imagePath = "images/backgrounds/background8.png";

  public Background8(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
