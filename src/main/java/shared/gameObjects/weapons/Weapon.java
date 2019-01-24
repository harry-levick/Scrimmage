package shared.gameObjects.weapons;

import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

/**
 * @author hlf764 The abstract class for all weapons in the game.
 */
abstract class Weapon extends GameObject {

  double damage;
  double weight; // grams
  String name;   // name of the weapon

  /**
   * Constructor of the weapon class
   *
   * @param _damage Damage of the weapon
   * @param _weight Weight of the weapon
   * @param _name Name of the weapon
   */
  public Weapon(double x, double y, ObjectID id, double _damage, double _weight, String _name) {
    super(x, y, id, "gunimagepath");
    setDamage(_damage);
    setWeight(_weight);
    setName(_name);
  }

  // -------------------
  // Setters and Getters
  // -------------------

  public double getDamage() {
    return this.damage;
  }

  public void setDamage(double newDamage) {
    if (newDamage > 0 && newDamage < 100.0f) {
      this.damage = newDamage;
    }
  }

  public double getWeight() {
    return this.weight;
  }

  public void setWeight(double newWeight) {
    if (newWeight > 0 && newWeight < 1000.0f) {
      this.weight = newWeight;
    }
  }

  public String getName() {
    return this.name;
  }

  public void setName(String newName) {
    this.name = newName;
  }
}
