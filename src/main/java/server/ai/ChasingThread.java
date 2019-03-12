package server.ai;

import java.util.List;
import server.ai.pathFind.AStar;
import shared.gameObjects.players.Player;

public class ChasingThread extends Thread {

  public List<boolean[]> plan;
  Bot bot;
  AStar pathFinder;
  Player targetPlayer;
  boolean running;
  int TIME_TO_SLEEP = 300;

  public ChasingThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler());
    running = true;
  }

  public void run() {
    while (running) {
      targetPlayer = bot.findTarget();

      List<boolean[]> tempList = pathFinder.optimise(targetPlayer, FSA.CHASING);
      plan.clear();
      plan.addAll(tempList);

      try {
        Thread.sleep(TIME_TO_SLEEP);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
  }

  public void terminate() {
    running = false;
  }

}
