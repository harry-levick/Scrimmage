package server.ai;

/** @author Harry Levick (hxl799) */
public enum FiniteStateAutomata {


  ATTACKING() {
    public FiniteStateAutomata next(PlayerState playerState) {
      int enemyDistance = playerState.getDistanceToEnemy();
      int weaponRange = playerState.getWeaponRange();
      int botHealth = playerState.getHealth();
      int enemyHealth = playerState.getEnemyHealth();

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
    public FiniteStateAutomata next(PlayerState playerState) {
      return null;
    }
  },
  CHASING_ATTACKING() {
    public FiniteStateAutomata next(PlayerState playerState) {
      return null;
    }
  },
  FLEEING() {
    public FiniteStateAutomata next(PlayerState playerState) {
      return null;
    }
  },
  FLEEING_ATTACKING() {
    public FiniteStateAutomata next(PlayerState playerState) {
      return null;
    }
  },
  STILL() {
    public FiniteStateAutomata next(PlayerState playerState) {
      return null;
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FiniteStateAutomata next(PlayerState playerState) {
      return FiniteStateAutomata.STILL;
    }
  };

  public abstract FiniteStateAutomata next(PlayerState playerState);
  final int highHealth = 66;
  final int mediumHealth = 33;

  FiniteStateAutomata() {

  }


}
