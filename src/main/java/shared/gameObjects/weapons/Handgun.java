package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;

/** @author hlf764 The Handgun class. */
public class Handgun extends Gun {

  private static String imagePath = "images/weapons/handgun.png";
  /**
   * Constructor of the Handgun class
   *
   * @param x The x position of the gun
   * @param y The y position of the gun
   * @param id The ObjectID of the gun
   * @param damage Damage of the gun
   * @param weight Weight of the gun
   * @param name Name of the gun
   * @param ammo Total amount of ammo
   * @param bulletSpeed Speed of the bullets
   * @param fireRate Fire rate of the gun (bullets per minute)
   * @param bulletWidth Width of the bullet
   * @param uuid UUID of the gun
   */
  public Handgun(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectID id,
      double damage,
      double weight,
      String name,
      int ammo,
      double bulletSpeed,
      int fireRate,
      double bulletWidth,
      UUID uuid) {
    super(
        x,
        y,
        sizeX,
        sizeY,
        id,
        damage,
        weight,
        name,
        ammo,
        bulletSpeed,
        fireRate,
        bulletWidth,
        false,
        true,
        uuid);
  }
  
  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      Bullet bullet = new HandgunBullet(getX(), getY(), 10, 10, mouseX, mouseY, this.bulletWidth, this.bulletSpeed,
          uuid);
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
  public void interpolatePosition(float alpha) {
    
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", imagePath);
  }
}
