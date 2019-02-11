package server.ai;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.InputHandler;
import java.util.List;
import java.util.stream.Collectors;
import server.ai.pathFind.AStar;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;

import java.util.UUID;
import shared.packets.PacketInput;
import shared.util.maths.Vector2;

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

  FSA state;
  Player targetPlayer;
  AStar pathFinder;
  List<Player> allPlayers;

  public Bot(double x, double y, double sizeX, double sizeY, UUID playerUUID,
      List<GameObject> allObjs) {
    super(x, y, sizeX, sizeY, playerUUID);
    this.state = FSA.INITIAL_STATE;

    // Collect all players (other than bots) from the world
    List<Player> allPlayers =
        allObjs.stream()
            .filter(p -> p instanceof Player)
            .map(Player.class::cast)
            .collect(Collectors.toList());

    targetPlayer = findTarget(allPlayers);
    this.pathFinder = new AStar(allObjs, this);
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

  @Override
  public void update() {
    // Transfer while loop from AiAgent.startAgent()
    double prevDist, newDist;

    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(), (float) targetPlayer.getY());
    // Calculate the distance to the target from the previous loop
    prevDist = botPos.exactMagnitude(targetPos);
    // Update the target player
    targetPlayer = findTarget(allPlayers);

    targetPos = new Vector2((float) targetPlayer.getX(), (float) targetPlayer.getY());
    // Calculate the distance to the updated target
    newDist = botPos.exactMagnitude(targetPos);

    state = state.next(targetPlayer, this, prevDist, newDist);

    switch (state) {
      case IDLE:
        // TODO what to do in the idle state?
        executeAction(new boolean[] {false, false, false, false, false});
      case CHASING:
        // Find the next best move to take, and execute this move.
        executeAction(pathFinder.optimise(targetPlayer));
        // TODO calculate and execute the best path to the target.
      case FLEEING:
        // TODO calculate and execute the best path away from the target.
      case ATTACKING:
        // TODO think about how an attacking script would work.
      case CHASING_ATTACKING:
        // TODO calculate and execute the best path to the target whilst attacking.
      case FLEEING_ATTACKING:
        // TODO calculate and execute the best path away from the target whilst attacking.
    }

    super.update();
  }

  /**
   * Finds the closest player
   *
   * @param allPlayers A list of all players in the world
   * @return The player who is the closest to the bot
   */
  private Player findTarget(List<Player> allPlayers) {
    Player target = null;
    double targetDistance = Double.POSITIVE_INFINITY;
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());

    for (Player p : allPlayers) {
      Vector2 playerPos = new Vector2((float) p.getX(), (float) p.getY());
      double distance = botPos.exactMagnitude(playerPos);
      // Update the target if another player is closer
      if (distance < targetDistance) {
        targetDistance = distance;
        target = p;
      }
    }
    return target;
  }

  /**
   * Receives an action and then executes this action. This method will only execute one action at a
   * time (the first action in the list). Since the method will be called inside of the agent loop
   *
   * @param action: an action to exacute.
   */
  private void executeAction(boolean[] action) {
    // TODO decide on the implementation of action execution
    this.jumpKey = action[Bot.KEY_JUMP];
    this.leftKey = action[Bot.KEY_LEFT];
    this.rightKey = action[Bot.KEY_RIGHT];
    this.click = action[Bot.KEY_CLICK];
  }

}

