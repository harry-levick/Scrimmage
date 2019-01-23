package shared.weapons;

/**
 * @author hlf764 The Handgun class.
 */
public class Handgun extends Gun {

  /**
   * Constructor of the Handgun class
   *
   * @param _damage Damage of the gun
   * @param _weight Weight of the gun
   * @param _name Name of the gun
   * @param _bulletSpeed Speed of the bullets
   * @param _fireRate Fire rate of the gun (bullets per minute)
   * @param _bulletWidth Width of the bullet
   * @param _range Range of the bullets they travel
   */
  public Handgun(
      float _damage,
      float _weight,
      String _name,
      float _bulletSpeed,
      float _range,
      float _fireRate,
      float _bulletWidth) {
    super(_damage, _weight, _name, _bulletSpeed, _fireRate, _bulletWidth, _range, false, false);
  }
}
