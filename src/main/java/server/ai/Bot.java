package server.ai;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

/** @author Harry Levick (hxl799) */

public class Bot extends Player {

  boolean jumpKey, leftKey, rightKey, click;
  double mouseX, mouseY;
  boolean mayJump = true;
  public int jumpTime = 0;

  public static final int KEY_JUMP = 0;
  public static final int KEY_LEFT = 1;
  public static final int KEY_RIGHT = 2;
  public static final int KEY_CLICK = 3;

  public Bot(double x, double y, ObjectID id, UUID playerUUID) {
    super(x, y, id, playerUUID);

  }

  public boolean mayJump() { return mayJump; }

}
