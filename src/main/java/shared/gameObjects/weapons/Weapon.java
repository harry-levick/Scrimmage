package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

/**
 * @author hlf764 The abstract class for all weapons in the game.
 */
public abstract class Weapon extends GameObject {

  /**
   *  Cooldown of a weapon = MAX_COOLDOWN - fireRate
   *  For every frame, deduct cooldown by 1
   *  If cooldown == 0, the weapon can be fired.
   *  Otherwise, nothing will happen when mouse is left-clicked
   */
  protected int MAX_COOLDOWN = 81;
  
  protected double damage;
  protected double weight; // grams
  protected String name; // name of the weapon
  protected boolean isGun;
  protected boolean isMelee;
  protected int ammo; // -1 = unlimited
  
  protected int currentCooldown;

  /**
   * Constructor of the weapon class
   *
   * @param damage Damage of the weapon
   * @param weight Weight of the weapon
   * @param name Name of the weapon
   */
  public Weapon(
      double x,
      double y,
      ObjectID id,
      double damage,
      double weight,
      String name,
      boolean isGun,
      boolean isMelee,
      int ammo,
      UUID uuid) {
    super(x, y, id, uuid);
    this.isGun = isGun;
    this.isMelee = isMelee;
    this.damage = damage;
    this.weight = weight;
    this.name = name;
    this.ammo = ammo;

    this.currentCooldown = 0;
  }

  public abstract void fire(double mouseX, double mouseY);
  public abstract int getCoolDown();
  
  public void deductCooldown() {
    if (this.currentCooldown > 0)
      this.currentCooldown -= 1;
  }
  
  public boolean canFire() {
    return this.currentCooldown <= 0;
  }

  // -------START-------
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

  public boolean isGun() {
    return isGun;
  }

  public void setIsGun(boolean gun) {
    isGun = gun;
  }

  public boolean isMelee() {
    return isMelee;
  }

  public void setIsMelee(boolean melee) {
    isMelee = melee;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public int getAmmo() {
    return this.ammo;
  }

  public void setAmmo(int newAmmo) {
    if (newAmmo == -1 || newAmmo > 0) {
      this.ammo = newAmmo;
    }
  }
  // -------------------
  // Setters and Getters
  // --------END--------
  
}
