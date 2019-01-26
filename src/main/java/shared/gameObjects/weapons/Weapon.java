package shared.gameObjects.weapons;

import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

/** @author hlf764 The abstract class for all weapons in the game. */
public abstract class Weapon extends GameObject {

  protected double damage;
  protected double weight; // grams
  protected String name; // name of the weapon
  protected boolean isGun;
  protected boolean isMelee;

  /**
   * Constructor of the weapon class
   *
   * @param damage Damage of the weapon
   * @param weight Weight of the weapon
   * @param name Name of the weapon
   */
  public Weapon(double x, double y, ObjectID id, double damage, double weight, String name,
      boolean isGun, boolean isMelee) {
    super(x, y, id, "gunimagepath");
    this.isGun = isGun;
    this.isMelee = isMelee;
    this.damage = damage;
    this.weight = weight;
    this.name = name;
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

  public boolean isGun() {
    return isGun;
  }

  public void setGun(boolean gun) {
    isGun = gun;
  }

  public boolean isMelee() {
    return isMelee;
  }

  public void setMelee(boolean melee) {
    isMelee = melee;
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
