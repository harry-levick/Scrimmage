package server.ai;

/** @author Harry Levick (hxl799) */
public enum FiniteStateAutomata {


  ATTACKING() {
    public FiniteStateAutomata next(PlayerState playerState) {
      if ( (playerState.getDistanceToEnemy() > playerState.getWeaponRange()) &&
          (playerState.getHealth() >= this.highHealth) || (playerState.getHealth() >= playerState.getEnemyHealth() * 1.4) ) {
        return CHASING;
      } else if ( (playerState.getDistanceToEnemy() <= playerState.getWeaponRange()) &&
          () ) {

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
