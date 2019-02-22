package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class Sword extends Melee {

  // private static String imagePath = "images/weapons/sword.jpg";
  private static String imagePath = "images/weapons/sword1.png";
  private int currentAngleIndex;

  /**
   * Constructor of the Sword class
   *
   * @param x The x position of the sword
   * @param y The y position of the sword
   * @param name Name of the sword
   * @param uuid The UUID of the sword
   */
  public Sword(double x, double y, String name, Player holder, UUID uuid) {
    super(
        x,
        y,
        50,
        50,
        ObjectID.Weapon,
        20, // damage
        10, // weight
        name,
        30, // ammo
        60, // fireRate
        holder,
        50, // range
        50, // beginAngle
        20, // endAngle
        uuid);
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    super.fire(mouseX, mouseY);
  }

  @Override
  public void update() {
    super.update();
  }

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());

    // set rotation of the sword
    if (this.attacking) {
      this.imageView.setRotate(45 + (-1 * getAngle(currentAngleIndex)));
      // set incrementation of angles for frames
      currentAngleIndex += 4;
      if (currentAngleIndex >= (int) (beginAngle + endAngle + 1)) {
        attacking = false;
        currentAngleIndex = 0;
        this.imageView.setRotate(0);
      }
    }
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize(
        "default", this.range, this.range, true, Path.convert(this.imagePath));
  }
}
