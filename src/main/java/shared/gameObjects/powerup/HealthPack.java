package shared.gameObjects.powerup;

import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class HealthPack extends Powerup {

  private static String imagePath = "images/powerups/healthPack.png";
  private static double sizeX = 30, sizeY = 30;
  private static int VALUE = 30;

  public HealthPack(
      double x,
      double y,
      UUID uuid) {
    super(x, y, sizeX, sizeY, "HealthPack", uuid);
  }

  @Override
  public void apply(Player p) {
    p.setHealth(p.getHealth() + VALUE);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert(imagePath));
  }
}
