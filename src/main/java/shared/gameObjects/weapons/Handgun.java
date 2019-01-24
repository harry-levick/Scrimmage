package shared.gameObjects.weapons;

import shared.gameObjects.Utils.ObjectID;

/** @author hlf764 The Handgun class. */
public class Handgun extends Gun {

  /**
   * Constructor of the Handgun class
   *
   * @param x The x position of the gun
   * @param y The y position of the gun
   * @param id The ObjectID of the gun
   * @param _damage Damage of the gun
   * @param _weight Weight of the gun
   * @param _name Name of the gun
   * @param _bulletSpeed Speed of the bullets
   * @param _fireRate Fire rate of the gun (bullets per minute)
   * @param _bulletWidth Width of the bullet
   */
  public Handgun(
      double x,
      double y,
      ObjectID id,
      double _damage,
      double _weight,
      String _name,
      double _bulletSpeed,
      double _fireRate,
      double _bulletWidth) {
    super(
        x,
        y,
        id,
        _damage,
        _weight,
        _name,
        _bulletSpeed,
        _fireRate,
        _bulletWidth,
        false,
        false);
  }

  public void update() {}

  public void render() {}
}
