package server.ai;

import java.util.UUID;
import shared.gameObjects.players.Player;

/**
 * @author Harry Levick (hxl799)
 */
public class Bot extends Player {

  public static final int KEY_JUMP = 0;
  public static final int KEY_LEFT = 1;
  public static final int KEY_RIGHT = 2;
  public static final int KEY_CLICK = 3;
  public int jumpTime = 0;
  boolean jumpKey, leftKey, rightKey, click;
  double mouseX, mouseY;
  boolean mayJump = true;

  public Bot(double x, double y, double sizeX, double sizeY, UUID playerUUID) {
    super(x, y, sizeX, sizeY, playerUUID);
  }

  public boolean mayJump() {
    return mayJump;
  }
}
