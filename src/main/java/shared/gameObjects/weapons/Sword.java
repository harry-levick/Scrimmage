package shared.gameObjects.weapons;

import shared.gameObjects.Utils.ObjectID;

public class Sword extends Melee {

  double range;
  double beginAngle;
  double endAngle;

  /**
   * Constructor of the Sword class
   *
   * @param x The x position of the sword
   * @param y The y position of the sword
   * @param id The ObjectID of the sword
   * @param _damage Damage of the sword
   * @param _weight Weight of the sword
   * @param _name Name of the sword
   * @param _range Range of the sword
   * @param _beginAngle The starting angle when the sword swing
   * @param _endAngle The ending angle when the sword swing
   */
  public Sword(
      double x,
      double y,
      ObjectID id,
      double _damage,
      double _weight,
      String _name,
      double _range,
      double _beginAngle,
      double _endAngle) {

    super(x, y, id, _damage, _weight, _name, _range, _beginAngle, _endAngle);
  }

  public void update() {

  }

  public void render() {

  }

}
