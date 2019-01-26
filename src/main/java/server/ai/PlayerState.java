package server.ai;

/** @author Harry Levick (hxl799) */
public class PlayerState {

  int health;
  int ammoLeft;
  int enemyHealth;
  int weaponRange;
  int enemyDist;
  int prevEnemyDist;

  PlayerState(int hp, int ammo, int enemy_hp, int weaponRange, int enemyDistance, int prevEnemyDist) {
    this.health = hp;
    this.ammoLeft = ammo;
    this.enemyHealth = enemy_hp;
    this.weaponRange = weaponRange;
    this.enemyDist = enemyDistance;
    this.prevEnemyDist =
  }

  public int getAmmoLeft() { return ammoLeft; }

  public int getHealth() { return health; }

  public int getEnemyDist() { return enemyDist; }

  public int getPrevEnemyDist() { return prevEnemyDist; }

  public int getEnemyHealth() { return enemyHealth; }

  public int getWeaponRange() { return weaponRange; }

}
