package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class Uzi extends Gun {

  private static String imagePath = "images/weapons/Test/Asset 8.png";
  private static double sizeX = 84, sizeY = 35;

  public Uzi(double x, double y, String name, Player holder, UUID uuid) {
    super(
        x,
        y,
        sizeX,
        sizeY,
        10, // weight
        name,
        50, // ammo
        70, // fireRate
        holder,
        true, // fullAutoFire
        false, // singleHanded
        uuid
    );
  }

  @Override
  public void fire(double mouseX, double mouseY) {

  }

  @Override
  public void render() {
    super.render();

    if (holder == null) return;

    imageView.getTransforms().clear();

    double mouseX = holder.mouseX;
    double mouseY = holder.mouseY;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert(this.imagePath));
  }

  @Override
  public double getGripX() {
    return 0;
  }

  @Override
  public double getGripY() {
    return 0;
  }

  @Override
  public double getGripFlipX() {
    return 0;
  }

  @Override
  public double getGripFlipY() {
    return 0;
  }

  @Override
  public double getForeGripX() {
    return 0;
  }

  @Override
  public double getForeGripY() {
    return 0;
  }

  @Override
  public double getForeGripFlipX() {
    return 0;
  }

  @Override
  public double getForeGripFlipY() {
    return 0;
  }
}
