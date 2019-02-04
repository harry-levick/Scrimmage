package server.ai;

import shared.gameObjects.players.Player;

import java.util.UUID;

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

  public Bot(double x, double y, double sizeX, double sizeY, UUID playerUUID) {
    super(x, y, sizeX, sizeY, playerUUID);
  }

  public boolean mayJump() {
    return mayJump;
  }
}
