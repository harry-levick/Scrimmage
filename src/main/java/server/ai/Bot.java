package server.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
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
  ChasingThread chasingThread;
  FleeingThread fleeingThread;
  List<boolean[]> chasingPlan;
  List<boolean[]> fleeingPlan;

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
    this.chasingPlan = Collections.synchronizedList(new ArrayList<>());
    this.fleeingPlan = Collections.synchronizedList(new ArrayList<>());

    this.chasingThread = new ChasingThread(this, chasingPlan);
    chasingThread.setName("Bot chasingThread");
    this.fleeingThread = new FleeingThread(this, fleeingPlan);

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
    chasingThread.start();
    fleeingThread.start();
  }

  public boolean mayJump() {
    return grounded;
  }

  @Override
  public void update() {

    if (!active) {
      chasingThread.terminate();
      fleeingThread.terminate();
    }
    click = false;

    double prevDist, newDist;
    // Calculate the distance to the target from the previous loop
    prevDist = calcDist();
    targetPlayer = findTarget();
    // Calculate the distance to the updated target
    newDist = calcDist();

    state = state.next(targetPlayer, this, prevDist, newDist);

    switch (state) {
      case IDLE:
        System.out.println("IDLE");
        break;
      case CHASING:
        //System.out.println("CHASING");
        executeAction(FSA.CHASING);

        break;
      case FLEEING:
        System.out.println("FLEEING");
        executeAction(FSA.FLEEING);

        break;
      case ATTACKING:
        System.out.println("ATTACKING");
        Vector2 enemyPosCenter = targetPlayer.getTransform().getPos()
            .add(targetPlayer.getTransform().getSize().mult(0.5f));
        mouseX = enemyPosCenter.getX();
        mouseY = enemyPosCenter.getY();
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
    Vector2 targetPos = new Vector2((float) targetPlayer.getX(),
        (float) targetPlayer.getY());
    // Calculate the distance to the target
    return botPos.exactMagnitude(targetPos);
  }

  private void executeAction(FSA state) {
    boolean[] action = new boolean[] {false, false, false};


    if (state == FSA.CHASING) {
      if (chasingPlan.size() > 0) action = chasingPlan.remove(0);

      this.jumpKey = action[Bot.KEY_JUMP];
      this.leftKey = action[Bot.KEY_LEFT];
      this.rightKey = action[Bot.KEY_RIGHT];

    } else if (state == FSA.FLEEING) {
      if (fleeingPlan.size() > 0) action = fleeingPlan.remove(0);
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
