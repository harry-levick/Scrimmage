package server.ai;

import java.util.List;
import server.ai.pathFind.AStar;
import shared.gameObjects.players.Player;

public class ChasingThread extends Thread {

  Bot bot;
  AStar pathFinder;
  Player targetPlayer;
  public List<boolean[]> plan;
  boolean running;

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

    }
    System.out.println("THREAD STOPPED");
  }

  public void terminate() {
    running = false;
  }

}
