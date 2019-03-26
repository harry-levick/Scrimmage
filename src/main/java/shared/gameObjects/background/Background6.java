package shared.gameObjects.background;

import java.util.UUID;

public class Background6 extends Background {

  private static final String imagePath = "images/backgrounds/background6.png";

  public Background6(UUID objectUUID) {
    super(objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
