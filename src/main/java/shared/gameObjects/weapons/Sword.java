package shared.gameObjects.weapons;

import shared.gameObjects.Utils.ObjectID;

public class Sword extends Melee {
  
  private static String imagePath = "";

  /**
   * Constructor of the Sword class
   *
   * @param x The x position of the sword
   * @param y The y position of the sword
   * @param damage Damage of the sword
   * @param weight Weight of the sword
   * @param name Name of the sword
   * @param range Range of the sword
   * @param beginAngle The starting angle when the sword swing
   * @param endAngle The ending angle when the sword swing
   */
  public Sword(
      double x,
      double y,
      double damage,
      double weight,
      String name,
      int ammo,
      double range,
      double beginAngle,
      double endAngle) {

    super(x, y, imagePath, damage, weight, name, ammo, range, beginAngle, endAngle);
  }

  public void update() {}

  public void render() {}
}
