package server.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import server.ai.pathFind.PathFindState;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/**
 * @author Harry Levick (hxl799)
 */
public class Bot extends Player {

  public static final int KEY_JUMP = 0;
  public static final int KEY_LEFT = 1;
  public static final int KEY_RIGHT = 2;
  public float jumpTime;

  FSA state;
  public Player targetPlayer;
  LinkedHashMap<UUID, Player> allPlayers;
  // Get the LevelHandler through the constructor
  LevelHandler levelHandler;
  BotThread botThread;
  List<boolean[]> plan;

  /**
   *
   * @param x x pos of the bot
   * @param y y pos of the bot
   * @param playerUUID
   * @param levelHandler
   */
  public Bot(double x, double y, UUID playerUUID,
      LevelHandler levelHandler) {
    super(x, y, playerUUID, levelHandler);
    this.state = FSA.INITIAL_STATE;
    this.levelHandler = levelHandler;
    this.targetPlayer = findTarget();
    this.plan = Collections.synchronizedList(new ArrayList<>());

    this.botThread = new BotThread(this, plan);
    botThread.setName("Bot Path-Finder");
  }

  /**
   * Copy constructor
   * @param that object to be copied
   */
  public Bot(Bot that) {
    super(that.getX(), that.getY(), UUID.randomUUID(), that.levelHandler);
    this.levelHandler = that.levelHandler;

  }

  public void startThread() {
    botThread.start();
  }

  public boolean mayJump() {
    return grounded;
  }

  @Override
  public void update() {

    if (!active) {
      botThread.terminate();
    }
    click = false;

    double prevDist, newDist;
    // Calculate the distance to the target from the previous loop
    prevDist = calcDist();
    targetPlayer = findTarget();
    // Calculate the distance to the updated target
    newDist = calcDist();

    //state = state.next(targetPlayer, this, prevDist, newDist);
    state = FSA.CHASING;

    switch (state) {
      case IDLE:
        System.out.println("IDLE");
        break;
      case CHASING:
        System.out.println("CHASING");
        // Find the next best move to take, and execute this move.
        executeAction(PathFindState.PERSUE);

        break;
      case FLEEING:
        System.out.println("FLEEING");
        executeAction(PathFindState.FLEE);
        // TODO calculate and execute the best path away from the target.

        break;
      case ATTACKING:
        System.out.println("ATTACKING");
        // TODO think about how an attacking script would work.
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();
        click = true;


        break;
      case CHASING_ATTACKING:
        System.out.println("CHASING-ATTACKING");
        executeAction(PathFindState.PERSUE);
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();

        break;
      case FLEEING_ATTACKING:
        System.out.println("FLEEING-ATTACKING");
        executeAction(PathFindState.FLEE);
        // TODO calculate and execute the best path away from the target whilst attacking.
        mouseX = targetPlayer.getX();
        mouseY = targetPlayer.getY();

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
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(),
        (float) targetPlayer.getY());
    // Calculate the distance to the target
    return botPos.exactMagnitude(targetPos);
  }

  private void executeAction(PathFindState state) {
    boolean[] action = new boolean[] {false, false, false};

    System.out.println("PLAN SIZE: " + plan.size());
    if (plan.size() > 0) {
      action = plan.remove(0);
    }

    if (state == PathFindState.PERSUE) {
      this.jumpKey = action[Bot.KEY_JUMP];
      this.leftKey = action[Bot.KEY_LEFT];
      this.rightKey = action[Bot.KEY_RIGHT];
      /*
      Random r = new Random();
      // 60% chance of jumping when asked to.
      boolean jump = r.nextDouble() <= 0.60;
      if (jump) {
        this.jumpKey = action[Bot.KEY_JUMP];
      } else this.jumpKey = false;

      // 60% chance of moving left when asked to.
      boolean left = r.nextDouble() <= 0.60;
      if (left) {
        this.leftKey = action[Bot.KEY_LEFT];
      } else this.leftKey = false;

      // 60% chance of moving right when asked to
      boolean right = r.nextDouble() <= 0.60;
      if (right) {
        this.rightKey = action[Bot.KEY_RIGHT];
      } else this.rightKey = false;

       */

    } else if (state == PathFindState.FLEE) {
      action = invertAction(action);
      this.jumpKey = action[Bot.KEY_JUMP];
      this.leftKey = action[Bot.KEY_LEFT];
      this.rightKey = action[Bot.KEY_RIGHT];
    }


  }

  /**
   * Invert a persuing action so that it can be used as a un-intelligente fleeing action
   * @param action
   * @return
   */
  private boolean[] invertAction(boolean[] action) {
    Random r = new Random();
    boolean move = r.nextDouble() <= 0.50;

    if (action[Bot.KEY_LEFT]) {
      action[Bot.KEY_LEFT] = false;
      action[Bot.KEY_RIGHT] = true;
      if (move) action[Bot.KEY_JUMP] = true;

    } else if (action[Bot.KEY_RIGHT]) {
      action[Bot.KEY_RIGHT] = false;
      action[Bot.KEY_LEFT] = true;
      if (move) action[Bot.KEY_JUMP] = true;
    }

    return action;
  }

  public void simulateUpdate() {
    super.update();
  }

  public void simulateAction(boolean[] action) {
    this.jumpKey = action[Bot.KEY_JUMP];
    this.leftKey = action[Bot.KEY_LEFT];
    this.rightKey = action[Bot.KEY_RIGHT];
  }

  public void simulateApplyInput() {
    if (rightKey) {
      rb.moveX(speed);
    }
    if (leftKey) {
      rb.moveX(speed * -1);
    }
    if (!rightKey && !leftKey) {
      vx = 0;
    }
    if (jumpKey && !jumped) {
      rb.moveY(jumpForce, 0.33333f);
      jumped = true;
    }
    if (grounded) {
      jumped = false;
    }
    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    }

    if (this.getHolding() != null) {
      this.getHolding().setX(this.getX() + 60);
      this.getHolding().setY(this.getY() + 70);
    }
  }

  /**
   * Finds the closest player
   * @return The player who is the closest to the bot
   */
  public Player findTarget() {
    allPlayers = levelHandler.getPlayers();

    Player target = null;
    double targetDistance = Double.POSITIVE_INFINITY;
    Vector2 botPos = new Vector2((float) this.getX(), (float) this.getY());


    for (Map.Entry<UUID, Player> entry : allPlayers.entrySet()) {
      Player player = entry.getValue();

      if (player.equals(this))
        continue;

      Vector2 playerPos = new Vector2((float) player.getX(), (float) player.getY());
      double distance = botPos.exactMagnitude(playerPos);
      // Update the target if another player is closer
      if (distance < targetDistance && player.isActive()) {
        targetDistance = distance;
        target = player;
      }
    }

    // Returns null if no active player is found.
    return target;
  }

  public LevelHandler getLevelHandler() {
    return levelHandler;
  }
}
