package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;

public class Sword extends Melee {

  private static String imagePath = "images/weapons/sword.jpg";

  /**
   * Constructor of the Sword class
   *
   * @param x The x position of the sword
   * @param y The y position of the sword
   * @param name Name of the sword
   * @param range Range of the sword
   * @param beginAngle The starting angle when the sword swing
   * @param endAngle The ending angle when the sword swing
   * @param uuid The UUID of the sword
   */
  public Sword(double x, double y, double sizeX, double sizeY, String name, double range,
      double beginAngle, double endAngle, UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectID.Weapon, 20, 10, name, 30,
        60, range, beginAngle, endAngle, uuid);
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      //swing
      this.currentCooldown = getDefaultCoolDown();
    }
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
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 50, 50, true, this.imagePath);
  }
}
