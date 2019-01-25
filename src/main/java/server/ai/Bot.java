package server.ai;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;

/** @author Harry Levick (hxl799) */
public class Bot extends Player {

  public Bot(double x, double y, ObjectID id) {
    super(x, y, id);

  }

  /**
   * Receives an action and then executes this action.
   */
  private void executeAction() {
    // Deciding on the implementation of action execution.
  }

}
