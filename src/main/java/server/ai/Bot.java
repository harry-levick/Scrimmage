package server.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import server.ai.pathFind.AStar;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/** TODO : When the bot is created, create the bot on a new thread, and run the update method on
 *         a constant while loop.
 */

/**
 * @author Harry Levick (hxl799)
 */
public class Bot extends Player {

  public static final int KEY_JUMP = 0;
  public static final int KEY_LEFT = 1;
  public static final int KEY_RIGHT = 2;
  public static final int KEY_CLICK = 3;
  public float jumpTime;
  boolean mayJump = true;

  FSA state;
  Player targetPlayer;
  AStar pathFinder;
  List<Player> allPlayers;
  // Get the LevelHandler through the constructor
  LevelHandler lh;

  /**
   * @param allObjs Contains a list of all game objects in the world, including players.
   */
  public Bot(double x, double y, double sizeX, double sizeY, UUID playerUUID,
      List<GameObject> allObjs) {
    super(x, y, sizeX, sizeY, playerUUID);
    allPlayers = new ArrayList<>();
    this.state = FSA.INITIAL_STATE;

    // Collect all players (other than bots) from the world
    allPlayers = allObjs.stream()
        .filter(p -> p instanceof Player)
        .map(Player.class::cast)
        .collect(Collectors.toList());

    targetPlayer = findTarget(allPlayers);
    this.pathFinder = new AStar(allObjs, this);
  }

  public boolean mayJump() {
    return mayJump;
  }

  @Override
  public void update() {
    double prevDist, newDist;
    // Calculate the distance to the target from the previous loop
    prevDist = calcDist();
    // Update the target player
    targetPlayer = findTarget(allPlayers);
    // Calculate the distance to the updated target
    newDist = calcDist();

    state = state.next(targetPlayer, this, prevDist, newDist);

    switch (state) {
      case IDLE:
        System.out.println("IDLE");
        executeAction(new boolean[]{false, false, false, false, false});
        click = false;

        break;
      case CHASING:
        System.out.println("CHASING");
        // Find the next best move to take, and execute this move.
        executeAction(pathFinder.optimise(targetPlayer));
        click = false;

        break;
      case FLEEING:
        System.out.println("FLEEING");
        executeAction(new boolean[]{false, false, false, false, false});
        // TODO calculate and execute the best path away from the target.
        click = false;

        break;
      case ATTACKING:
        System.out.println("ATTACKING");
        // TODO think about how an attacking script would work.
        Collision inSight = Physics.raycast(new Vector2((float) this.getX(), (float) this.getY()),
            new Vector2((float) targetPlayer.getX(), (float) targetPlayer.getY()));
        // If the target player is in sight of the bot, they can shoot.
        if (inSight == null) {
          mouseX = targetPlayer.getX();
          mouseY = targetPlayer.getY();
          click = true;
        }

        break;
      case CHASING_ATTACKING:
        System.out.println("CHASING-ATTACKING");
        //executeAction(pathFinder.optimise(targetPlayer));
        // TODO calculate and execute the best path to the target whilst attacking.
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();
        click = true;

        break;
      case FLEEING_ATTACKING:
        System.out.println("CHASING-ATTACKING");
        executeAction(new boolean[]{false, false, false, false, false});
        // TODO calculate and execute the best path away from the target whilst attacking.
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();
        click = true;

        break;
    }

    super.update();
  }

  /**
   * Calculates the distance from the bot to the current target player
   *
   * @return The distance to the target player
   */
  private double calcDist() {
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(), (float) targetPlayer.getY());
    // Calculate the distance to the target
    return botPos.exactMagnitude(targetPos);
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
      if (distance < targetDistance && p.isActive()) {
        targetDistance = distance;
        target = p;
      }
    }
    // Returns null if no active player is found.
    return target;
  }

  /**
   * Receives an action and then executes this action. This method will only execute one action at a
   * time (the first action in the list). Since the method will be called inside of the agent loop
   *
   * @param action: an action to exacute.
   */
  private void executeAction(boolean[] action) {

    Random r = new Random();
    // 75% chance of jumping when asked to.
    boolean jump = r.nextDouble() <= 0.75;
    if (jump) {
      this.jumpKey = action[Bot.KEY_JUMP];
    } else this.jumpKey = false;

    // 80% chance of moving left when asked to.
    boolean left = r.nextDouble() <= 0.8;
    if (left) {
      this.leftKey = action[Bot.KEY_LEFT];
    } else this.leftKey = false;

    // 80% chance of moving right when asked to
    boolean right = r.nextDouble() <= 0.8;
    if (right) {
      this.rightKey = action[Bot.KEY_RIGHT];
    } else this.rightKey = false;

    // 50% chance of shooting when asked to
    boolean shoot = r.nextDouble() <= 0.5;
    if (shoot) {
      this.click = action[Bot.KEY_CLICK];
    } else this.click = false;
  }

}

