package server.ai.pathFind;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.Test;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;

public class AStarTest {

  @Test
  public void testStraightLineRight() {
    ArrayList<GameObject> allObjs = new ArrayList<GameObject>();

    Player testPlayer = new Player(50, 20, UUID.randomUUID(), null);
    Bot testBot = new Bot(20, 20, UUID.randomUUID(), allObjs, null);

    allObjs.add(testBot);
    allObjs.add(testPlayer);

    AStar AStarTest = new AStar(allObjs, testBot);

    boolean[] action = AStarTest.optimise(testPlayer);

    while (action[Bot.KEY_LEFT] || action[Bot.KEY_RIGHT] || action[Bot.KEY_JUMP]) {

      if (action[Bot.KEY_JUMP]) {
        // System.out.println("JUMP");
      }
      if (action[Bot.KEY_RIGHT]) {
        // System.out.println("RIGHT");
      }
      if (action[Bot.KEY_LEFT]) {
        // System.out.println("LEFT");
      }
      if (!action[Bot.KEY_JUMP] && !action[Bot.KEY_RIGHT] && !action[Bot.KEY_LEFT]) {
        // System.out.println("DO NOTHING");
      }

      action = AStarTest.optimise(testPlayer);

      // System.out.println("------------------");

    }
  }
}
