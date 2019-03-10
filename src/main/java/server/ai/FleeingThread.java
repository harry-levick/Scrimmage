package server.ai;

import java.util.List;
import server.ai.pathFind.AStar;
import shared.gameObjects.players.Player;

public class FleeingThread extends Thread {

  Bot bot;
  AStar pathFinder;
  Player targetPlayer;
  public List<boolean[]> plan;
  boolean running;

  public FleeingThread(Bot bot, List<boolean[]> plan) {
    this.bot = bot;
    this.plan = plan;
    pathFinder = new AStar(bot, bot.getLevelHandler());
    running = true;
  }

  public void run() {
    while (running) {

      targetPlayer = bot.findTarget();
      List<boolean[]> tempList = pathFinder.optimise(targetPlayer,  FSA.FLEEING);
      plan.clear();
      plan.addAll(tempList);

    }
  }

  public void terminate() {
    running = false;
  }

}
