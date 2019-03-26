package shared.gameObjects.background;

import java.util.UUID;

public class Background2 extends Background {

  private static final String imagePath = "images/backgrounds/background2.png";

  public Background2(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
