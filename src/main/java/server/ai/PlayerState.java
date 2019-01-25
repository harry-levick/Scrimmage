package server.ai;

/** @author Harry Levick (hxl799) */
public class PlayerState {

  private int health;
  private int ammoLeft;
  private int enemyHealth;

  PlayerState(int hp, int ammo, int enemy_hp) {
    this.health = hp;
    this.ammoLeft = ammo;
    this.enemyHealth = enemy_hp;
  }

}
