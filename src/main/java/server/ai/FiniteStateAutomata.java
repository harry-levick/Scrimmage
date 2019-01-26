package server.ai;

import shared.gameObjects.GameObject;

/** @author Harry Levick (hxl799) */
public enum FiniteStateAutomata {


  ATTACKING() {
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      int enemyDistance;
      int weaponRange;
      int botHealth;
      int enemyHealth;

      if ( (enemyDistance > weaponRange) && (botHealth >= this.highHealth) ||
          (enemyDistance > weaponRange) && (botHealth >= enemyHealth * 1.5) ) {
        return CHASING;

      } else if ( (enemyDistance <= weaponRange) &&
          () ) {
        return CHASING_ATTACKING;

      } else if () {
        return FLEEING;

      } else if () {
        return FLEEING_ATTACKING;

      }
    }

  },
  CHASING() {
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      return null;
    }
  },
  CHASING_ATTACKING() {
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      return null;
    }
  },
  FLEEING() {
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      return null;
    }
  },
  FLEEING_ATTACKING() {
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      return null;
    }
  },
  STILL() {
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      return null;
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FiniteStateAutomata next(GameObject targetPlayer, Bot bot) {
      return FiniteStateAutomata.STILL;
    }
  };

  public abstract FiniteStateAutomata next(GameObject targetPlayer, Bot bot);
  final int highHealth = 66;
  final int mediumHealth = 33;

  FiniteStateAutomata() {

  }


}
