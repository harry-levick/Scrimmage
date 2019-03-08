package shared.gameObjects.weapons;

import client.main.Client;
import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.types.RigidbodyType;
import shared.util.Path;
import shared.util.maths.Vector2;

public class ExplosiveBullet extends Bullet {

  private static String imagePath = "images/weapons/explosiveBullet.png";
  private static final int width = 15;          // Width of the bullet
  private static final int damage = 20;         // Damage of the explosion
  private static final int speed = 25;          // Speed of bullet travelling
  private static final float radius = 40f;      // Radius of explosion
  private static final float pushPower = 50f;   // Power of pushing on impact

  public ExplosiveBullet (
      double gunX,
      double gunY,
      double mouseX,
      double mouseY,
      Player holder,
      UUID uuid) {

    super(gunX, gunY, mouseX, mouseY, width, speed, damage, holder, uuid);
  }

  @Override
  public void initialiseAnimation() {
    // this.animation.supplyAnimation("default", this.imagePath);
    this.animation.supplyAnimationWithSize(
        "default", this.getWidth(), this.getWidth(), true, Path.convert(imagePath));
  }

  @Override
  /**
   * Holder: Not going to take damage, Will be knocked back by explosion but not on direct impact
   * In explosion area, every player will take half the damage this bullet dealt
   * Player on direct impact will take half the damage on collision, then half the damage on
   *   explosion
   *
   * @param col The collision on direct impact
   *
   */
  public void OnCollisionEnter(Collision col) {
    ArrayList<Collision> collision = Physics.circlecastAll(this.bc.getCentre(), radius);
    GameObject gCol = col.getCollidedObject();
    Player pCol;
    boolean remove = true;

    if (gCol.getId() == ObjectType.Player) {
      pCol = (Player) gCol;
      if (pCol.equals(holder)) {
        hitHolder = true;
        remove = false;
      }
      else {
        // Player on direct impact takes full damage (another half dealt in circleCasting down there)
        pCol.deductHp(damage/2);
      }
    }

    for (Collision c : collision) {
      GameObject g = c.getCollidedObject();

      // Skip if getCollidedObject gets removed accidentally
      if (g == null) continue;

      // Not going to push holder if he is the first collided object (i.e. the impact)
      if (g.equals(gCol) && hitHolder) { continue; }

      // Not going to deal damage to holder
      if (g.getId() == ObjectType.Player && !g.equals(holder)) {
        // Every player in the explosion area deals half the damage
        ((Player) g).deductHp(damage/2);
      }

      // Knockback here
      Rigidbody body = (Rigidbody) g.getComponent(ComponentType.RIGIDBODY);

      if (body != null && body.getBodyType() == RigidbodyType.DYNAMIC) {
        Vector2 dir = g.getTransform().getPos().sub(bc.getCentre()).normalize();
        body.move(dir.mult(pushPower), 0.1f);
      }

    }

    if (remove)
      Client.levelHandler.removeGameObject(this);
  }

}
