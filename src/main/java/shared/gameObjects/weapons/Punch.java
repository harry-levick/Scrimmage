package shared.gameObjects.weapons;

import java.util.HashSet;
import java.util.UUID;
import shared.gameObjects.Destructable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.util.Path;
import shared.util.maths.Vector2;

/**
 * Default holding weapon of a player
 */
public class Punch extends Melee {

  /**
   * Size of the image (default image = blank)
   */
  private static double sizeX = 30, sizeY = 30;
  /**
   * Range of punch
   */
  private static double range = 50;

  /**
   * Constructor for Punch
   *
   * @param x x position of player's hand
   * @param y y position of player's hand
   * @param name Name
   * @param holder Player this punch refers to
   * @param uuid UUID
   */
  public Punch(
      double x,
      double y,
      String name,
      Player holder,
      UUID uuid) {
    super(x,
        y,
        sizeX,
        sizeY,
        ObjectType.Fist,
        10,
        1,
        name,
        -1,
        60,
        0, // pivotX
        0, // pivotY
        holder,
        range,
        1,
        1,
        true,
        uuid);
    this.weaponRank = 0;
  }

  /**
   * Duplicate a Punch with different UUID
   *
   * @param that Source of duplication
   */
  public Punch(Punch that) {
    this(that.getX(), that.getY(), that.name, that.holder, UUID.randomUUID());
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      Vector2 boxCastSize = new Vector2(10f, 10f);
      HashSet<Collision> collisionSet = new HashSet<>();

      // Get the angle of punching
      Vector2 vPunch = new Vector2(mouseX - getGripX(), mouseY - getGripY());
      vPunch = vPunch.normalize().mult((float) range);

      int numCast = (int) Math.abs(vPunch.getX()) / 10 + 1;
      double deltaX = vPunch.getX() / numCast;
      double deltaY = vPunch.getY() / numCast;

      for (int i = 0; i < numCast; i++) {
        collisionSet.addAll(
            new HashSet<>(
                Physics.boxcastAll(
                    new Vector2((float) (this.getGripX() + (i * deltaX)),
                        (float) (this.getGripY() + (i * deltaY))),
                    boxCastSize,
                    true // TODO: set to false
                )
            )
        );
      }

      for (Collision c : collisionSet) {
        GameObject g = c.getCollidedObject();
        if (g instanceof Destructable && !g.equals(holder)) {
          ((Destructable) g).deductHp(this.damage);
        }
      }

      this.currentCooldown = getDefaultCoolDown();
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert("images/player/player_idle.png"));
  }

  @Override
  public double getGripX() {
    if (holder.isPointingLeft()) {
      return getGripFlipX();
    }
    return holder.getHandRight().getX();
  }

  @Override
  public double getGripY() {
    if (holder.isPointingLeft()) {
      return getGripFlipY();
    }
    return holder.getHandRight().getY();
  }

  @Override
  public double getGripFlipX() {
    return holder.getHandLeft().getX();
  }

  @Override
  public double getGripFlipY() {
    return holder.getHandLeft().getY();
  }
}
