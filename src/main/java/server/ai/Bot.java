package server.ai;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import shared.gameObjects.players.Player;

import java.util.UUID;
import shared.packets.PacketInput;

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

  public void applyInput(boolean multiplayer, ConnectionHandler connectionHandler) {
    if (this.rightKey) {
      vx = speed;
      animation.switchAnimation("moveRight");
    }
    if (this.leftKey) {
      vx = -speed;
      animation.switchAnimation("moveLeft");
    }

    if (!this.rightKey && !this.leftKey) {
      vx = 0;
      animation.switchDefault();
    }
    if (this.click && holding != null) {
      holding.fire(InputHandler.x, InputHandler.y);
    } //else punch
    setX(getX() + (vx * 0.0166));

    /** If multiplayer then send input to server */
    if (multiplayer) {
      PacketInput input = new PacketInput(InputHandler.x, InputHandler.y, InputHandler.leftKey,
          InputHandler.rightKey, InputHandler.jumpKey, InputHandler.click);
      connectionHandler.send(input.getData());
    }
  }

}
