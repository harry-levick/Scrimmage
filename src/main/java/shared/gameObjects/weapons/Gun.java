package shared.gameObjects.weapons;

import shared.gameObjects.Utils.ObjectID;

/** @author hlf764 The abstract class for all guns type weapon. */
abstract class Gun extends Weapon {

  double bulletSpeed; // pixel per second
  double fireRate; // bullets per minute
  double bulletWidth; // width of bullet ==(pixel?)==
  boolean fullAutoFire; // able to shoot with full-auto or single-shot
  boolean singleHanded; // holding the weapon with one hand or two

  /**
   * Constructor of the Gun class
   *
   * @param _damage Damage of the gun
   * @param _weight Weight of the gun
   * @param _name Name of the gun
   * @param _bulletSpeed Speed of the bullets
   * @param _fireRate Fire rate of the gun (bullets per minute)
   * @param _bulletWidth Width of the bullet
   * @param _fullAutoFire Is it full-automatic fire or single-shot
   * @param _singleHanded Is it be hold with one hand or two hands
   */
  public Gun(
      double x,
      double y,
      ObjectID id,
      double _damage,
      double _weight,
      String _name,
      double _bulletSpeed,
      double _fireRate,
      double _bulletWidth,
      boolean _fullAutoFire,
      boolean _singleHanded) {

    super(x, y, id, _damage, _weight, _name);
    setBulletSpeed(_bulletSpeed);
    setFireRate(_fireRate);
    setBulletWidth(_bulletWidth);
    this.fullAutoFire = _fullAutoFire;
    this.singleHanded = _singleHanded;
  }

  // -------------------
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

  public double getFireRate() {
    return this.fireRate;
  }

  public void setFireRate(double newFireRate) {
    if (newFireRate > 0) {
      this.fireRate = newFireRate;
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
