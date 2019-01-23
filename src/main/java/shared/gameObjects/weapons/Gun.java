package shared.gameObjects.weapons;

import shared.gameObjects.Utils.ObjectID;

/**
 * @author hlf764 The abstract class for all guns type weapon.
 */
abstract class Gun extends Weapon {

  double bulletSpeed = 5.0;     // metre per second
  double fireRate = 80.0;       // bullets per minute
  double bulletWidth = 30.0;    // width of bullet ==(pixel?)==
  double range = 1000.0;        // suitable for swords, explosion, laser beams
  boolean fullAutoFire = false; // able to shoot with full-auto or single-shot
  boolean singleHanded = false; // holding the weapon with one hand or two

  /**
   * Constructor of the Gun class
   *
   * @param _damage         Damage of the gun
   * @param _weight         Weight of the gun
   * @param _name           Name of the gun
   * @param _bulletSpeed    Speed of the bullets
   * @param _fireRate       Fire rate of the gun (bullets per minute)
   * @param _bulletWidth    Width of the bullet
   * @param _range          Range of the bullets they travel
   * @param _fullAutoFire   Is it full-automatic fire or single-shot
   * @param _singleHanded   Is it be hold with one hand or two hands
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
      double _range,
      boolean _fullAutoFire,
      boolean _singleHanded) {
    super(x, y, id, _damage, _weight, _name);
    setBulletSpeed(_bulletSpeed);
    setFireRate(_fireRate);
    setBulletWidth(_bulletWidth);
    setRange(_range);
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
    if (newSpeed > 0 && newSpeed < 50.0f) {
      this.bulletSpeed = newSpeed;
    }
  }

  public double getFireRate() {
    return this.fireRate;
  }

  public void setFireRate(double newFireRate) {
    if (newFireRate > 0f) {
      this.fireRate = newFireRate;
    }
  }

  public double getBulletWidth() {
    return this.bulletWidth;
  }

  public void setBulletWidth(double newWidth) {
    if (newWidth > 0f) {
      this.bulletWidth = newWidth;
    }
  }

  public double getRange() {
    return this.range;
  }

  public void setRange(double newRange) {
    if (newRange > 0) {
      this.range = newRange;
    }
  }

  public boolean isFullAutoFire() {
    return this.fullAutoFire;
  }

  public boolean isSingleHanded() {
    return this.singleHanded;
  }

  /**
   * For testing
   */
  @Override
  public String toString() {
    String s = "";

    s += "Damage        = " + getDamage() + "\n";
    s += "Weight        = " + getWeight() + "\n";
    s += "Name          = " + getName() + "\n";
    s += "BulletSpeed   = " + getBulletSpeed() + "\n";
    s += "Range         = " + getRange() + "\n";
    s += "FireRate      = " + getFireRate() + "\n";
    s += "BulletWidth   = " + getBulletWidth() + "\n";
    s += "FullAutoFire  = " + isFullAutoFire() + "\n";
    s += "SingleHanded  = " + isSingleHanded() + "\n";

    return s;
  }
}
