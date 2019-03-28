package server.ai;

import java.util.List;
import server.ai.pathFind.AStar;
import shared.gameObjects.players.Player;

/**
 * Thread to handle Bot fleeing behaviour
 */
public class FleeingThread extends Thread {

  /** The respecive bot */
  Bot bot;
  /** The path-finder used to find the best path */
  AStar pathFinder;
  /** The bots target */
  Player targetPlayer;
  /** The shared path with the bot */
  public List<boolean[]> plan;
  /** Flag that governs the threads main loop */
  boolean running;
  /** The time delay used at the end of each loop */
  int TIME_TO_SLEEP = 300;

  /**
   * Create a new thread
   * @param bot The respective bot
   * @param plan The shared plan
   */
  public FleeingThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler());
    running = true;
    Thread.currentThread().setName("Fleeing Calculator " + bot.getUUID());
  }

  @Override
  public void run() {
    while (running) {

      targetPlayer = bot.getTargetPlayer();
      List<boolean[]> tempList = pathFinder.optimise(targetPlayer, FSA.FLEEING);
      plan.clear();
      plan.addAll(tempList);

      try {
        Thread.sleep(TIME_TO_SLEEP);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
  }

  /**
   * Terminated the main thread loop
   */
  public void terminate() {
    running = false;
  }

}
