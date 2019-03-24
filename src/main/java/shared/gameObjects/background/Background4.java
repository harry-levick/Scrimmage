package shared.gameObjects.background;

import java.util.UUID;

public class Background4 extends Background {

  private static final String imagePath = "images/backgrounds/background4.png";

  public Background4(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
