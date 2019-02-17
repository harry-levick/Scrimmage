package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import client.main.Client;

/**
 * @author hlf764 The abstract class for all weapons in the game.
 */
public abstract class Weapon extends GameObject {

  /**
   * Cooldown of a weapon = MAX_COOLDOWN - fireRate For every frame, deduct cooldown by 1 If
   * cooldown == 0, the weapon can be fired. Otherwise, nothing will happen when mouse is
   * left-clicked
   */
  protected int MAX_COOLDOWN = 81;

  protected int damage;
  protected double weight; // grams
  protected String name; // name of the weapon
  protected boolean isGun;
  protected boolean isMelee;
  protected int ammo; // -1 = unlimited
  protected int fireRate;   // max = MAX_COOLDOWN - 1

  protected int currentCooldown;

  /**
   * Constructor of the weapon class
   *
   * @param x X position of this weapon
   * @param y Y position of this weapon
   * @param id ObjectID of this weapon
   * @param damage Damage of this weapon
   * @param weight Weight of this weapon
   * @param name Name of this weapon
   * @param isGun True if this weapon is a gun
   * @param isMelee True if this weapon is a melee
   * @param ammo Ammo of this weapon (> 0)
   * @param fireRate FireRate of this weapon
   * @param uuid UUID of this weapon
   */
  public Weapon(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectID id,
      int damage,
      double weight,
      String name,
      boolean isGun,
      boolean isMelee,
      int ammo,
      int fireRate,
      UUID uuid) {
    super(x, y, sizeX, sizeY, id, uuid);
    this.isGun = isGun;
    this.isMelee = isMelee;
    setDamage(damage);
    setWeight(weight);
    this.name = name;
    setAmmo(ammo);
    setFireRate(fireRate);

    this.currentCooldown = 0;
  }

  public abstract void fire(double mouseX, double mouseY);


  public int getDefaultCoolDown() {
    return MAX_COOLDOWN - this.fireRate;
  }

  public void deductCooldown() {
    if (this.currentCooldown > 0) {
      this.currentCooldown -= 1;
    }
  }
  
  public void deductAmmo() {
    if (this.ammo > 0)
      this.ammo -= 1;
  }

  public boolean canFire() {
    return this.currentCooldown <= 0;
  }
  
  public void destroyWeapon() {
    Client.levelHandler.delGameObject(this);
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  public int getCoolDown() {
    return this.currentCooldown;
  }
  
  public int getDamage() {
    return this.damage;
  }

  public void setDamage(int newDamage) {
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

  public int getFireRate() {
    return this.fireRate;
  }

  public void setFireRate(int newFireRate) {
    if (newFireRate > 0) {
      this.fireRate = newFireRate;
    }
  }
  // -------------------
  // Setters and Getters
  // --------END--------

}
