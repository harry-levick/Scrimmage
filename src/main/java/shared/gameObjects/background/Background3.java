package shared.gameObjects.background;

import java.util.UUID;

public class Background3 extends Background {

  private final String imagePath = "images/backgrounds/background3.png";

  public Background3(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
