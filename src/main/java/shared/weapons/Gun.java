package shared.weapons;

/**
 * @author hlf764 The abstract class for all guns type weapon.
 */
abstract class Gun extends Weapon {

  float bulletSpeed = 5.0f; // metre per second
  float fireRate = 80.0f; // bullets per minute
  float bulletWidth = 30.0f; // width of bullet ==(pixel?)==
  float range = 1000.0f; // suitable for swords, explosion, laser beams
  boolean fullAutoFire = false; // able to shoot with full-auto or single-shot
  boolean singleHanded = false; // holding the weapon with one hand or two

  /**
   * Constructor of the Gun class
   *
   * @param _damage Damage of the gun
   * @param _weight Weight of the gun
   * @param _name Name of the gun
   * @param _bulletSpeed Speed of the bullets
   * @param _fireRate Fire rate of the gun (bullets per minute)
   * @param _bulletWidth Width of the bullet
   * @param _range Range of the bullets they travel
   * @param _fullAutoFire Is it full-automatic fire or single-shot
   * @param _singleHanded Is it be hold with one hand or two hands
   */
  public Gun(
      float _damage,
      float _weight,
      String _name,
      float _bulletSpeed,
      float _fireRate,
      float _bulletWidth,
      float _range,
      boolean _fullAutoFire,
      boolean _singleHanded) {
    super(_damage, _weight, _name);
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

  public float getBulletSpeed() {
    return this.bulletSpeed;
  }

  public void setBulletSpeed(float newSpeed) {
    if (newSpeed > 0 && newSpeed < 50.0f) {
      this.bulletSpeed = newSpeed;
    }
  }

  public float getFireRate() {
    return this.fireRate;
  }

  public void setFireRate(float newFireRate) {
    if (newFireRate > 0f) {
      this.fireRate = newFireRate;
    }
  }

  public float getBulletWidth() {
    return this.bulletWidth;
  }

  public void setBulletWidth(float newWidth) {
    if (newWidth > 0f) {
      this.bulletWidth = newWidth;
    }
  }

  public float getRange() {
    return this.range;
  }

  public void setRange(float newRange) {
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
