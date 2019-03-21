package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.Path;

/**
 * Default holding weapon of a player
 */
public class Punch extends Melee {

  private static double sizeX = 30, sizeY = 30;
  private static double range = 30;

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
  }

  public Punch(Punch that) {
    this(that.getX(), that.getY(), that.name, that.holder, UUID.randomUUID());
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    super.fire(mouseX, mouseY);
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", Path.convert("images/player/player_idle.png"));
  }

  @Override
  public double getGripX() {
    return holder.getMeleeHandPos()[0];
  }

  @Override
  public double getGripY() {
    return holder.getMeleeHandPos()[1];
  }

  @Override
  public double getGripFlipX() {
    return holder.getMeleeHandPos()[0];
  }

  @Override
  public double getGripFlipY() {
    return holder.getMeleeHandPos()[1];
  }
}
