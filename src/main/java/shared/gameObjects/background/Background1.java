package shared.gameObjects.background;

import java.util.UUID;

public class Background1 extends Background {

  private final String imagePath = "images/backgrounds/background1.png";

  public Background1(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
