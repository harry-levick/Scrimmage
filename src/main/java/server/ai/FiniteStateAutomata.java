package server.ai;

/** @author Harry Levick (hxl799) */
public enum FiniteStateAutomata {

  ATTACKING() {
    public FiniteStateAutomata next() {
      return null;
    }

  },
  CHASING() {
    public FiniteStateAutomata next() {
      return null;
    }
  },
  CHASING_ATTACKING() {
    public FiniteStateAutomata next() {
      return null;
    }
  },
  FLEEING() {
    public FiniteStateAutomata next() {
      return null;
    }
  },
  FLEEING_ATTACKING() {
    public FiniteStateAutomata next() {
      return null;
    }
  },
  STILL() {
    public FiniteStateAutomata next() {
      return null;
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FiniteStateAutomata next() {
      return FiniteStateAutomata.STILL;
    }
  };

  public abstract FiniteStateAutomata next();

  FiniteStateAutomata() {

  }


}
