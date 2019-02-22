package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

/** @author hlf764 The abstract class for all guns type weapon. */
abstract class Gun extends Weapon {

  protected double bulletSpeed; // pixel per second
  protected int fireRate; // bullets per minute
  protected double bulletWidth; // width of bullet ==(pixel?)==
  protected boolean fullAutoFire; // able to shoot with full-auto or single-shot
  protected boolean singleHanded; // holding the weapon with one hand or two
  protected int ammo; // The amount of ammo left

  /**
   * Constructor of the Gun class
   *
   * @param x The x position of the gun
   * @param y The y position of the gun
   * @param damage Damage of the gun
   * @param weight Weight of the gun
   * @param name Name of the gun
   * @param ammo Total amount of ammo
   * @param bulletSpeed Speed of the bullets
   * @param fireRate Fire rate of the gun (bullets per minute)
   * @param bulletWidth Width of the bullet
   * @param fullAutoFire Is it full-automatic fire or single-shot
   * @param singleHanded Is it be hold with one hand or two hands
   */
  public Gun(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectID id,
      int damage,
      double weight,
      String name,
      int ammo,
      double bulletSpeed,
      int fireRate,
      double bulletWidth,
      Player holder,
      boolean fullAutoFire,
      boolean singleHanded,
      UUID uuid) {

    super(x, y, sizeX, sizeY, id, damage, weight, name, true, false, ammo, fireRate, holder, uuid);

    this.bulletSpeed = bulletSpeed;
    this.bulletWidth = bulletWidth;
    this.fullAutoFire = fullAutoFire;
    this.singleHanded = singleHanded;
  }

  @Override
  public void update() {
    deductCooldown();
    super.update();
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  public double getBulletSpeed() {
    return this.bulletSpeed;
  }

  public void setBulletSpeed(double newSpeed) {
    if (newSpeed > 0 && newSpeed < 50.0) {
      this.bulletSpeed = newSpeed;
    }
  }

  public double getBulletWidth() {
    return this.bulletWidth;
  }

  public void setBulletWidth(double newWidth) {
    if (newWidth > 0) {
      this.bulletWidth = newWidth;
    }
  }

  public boolean isFullAutoFire() {
    return this.fullAutoFire;
  }

  public boolean isSingleHanded() {
    return this.singleHanded;
  }

  // -------------------
  // Setters and Getters
  // --------END--------

  /** For testing */
  @Override
  public String toString() {
    String s = "";

    s += "Damage        = " + getDamage() + "\n";
    s += "Weight        = " + getWeight() + "\n";
    s += "Name          = " + getName() + "\n";
    s += "BulletSpeed   = " + getBulletSpeed() + "\n";
    s += "FireRate      = " + getFireRate() + "\n";
    s += "BulletWidth   = " + getBulletWidth() + "\n";
    s += "FullAutoFire  = " + isFullAutoFire() + "\n";
    s += "SingleHanded  = " + isSingleHanded() + "\n";

    return s;
  }
}
