package server.ai;

/** @author Harry Levick (hxl799) */
public class PlayerState {

  int health;
  int ammoLeft;
  int enemyHealth;
  int weaponRange;
  int distanceToEnemy;

  PlayerState(int hp, int ammo, int enemy_hp, int weaponRange) {
    this.health = hp;
    this.ammoLeft = ammo;
    this.enemyHealth = enemy_hp;
    this.weaponRange = weaponRange;
  }

  public int getAmmoLeft() { return ammoLeft; }

  public int getHealth() { return health; }

  public int getDistanceToEnemy() { return distanceToEnemy; }

  public int getEnemyHealth() { return enemyHealth; }

  public int getWeaponRange() { return weaponRange; }

}
