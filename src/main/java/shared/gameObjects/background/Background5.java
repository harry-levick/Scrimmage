package shared.gameObjects.background;

import java.util.UUID;

public class Background5 extends Background {

  private static final String imagePath = "images/backgrounds/background5.png";

  public Background5(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
