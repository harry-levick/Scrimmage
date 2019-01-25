package server.ai;

public enum FiniteStateAutomata {

  ATTACKING() {
    public FiniteStateAutomata next() {

    }

  },
  CHASING() {
    public FiniteStateAutomata next() {

    }
  },
  CHASING_ATTACKING() {
    public FiniteStateAutomata next() {

    }
  },
  FLEEING() {
    public FiniteStateAutomata next() {

    }
  },
  FLEEING_ATTACKING() {
    public FiniteStateAutomata next() {

    }
  },
  STILL() {
    public FiniteStateAutomata next() {

    }
  };

  public abstract FiniteStateAutomata next();


}
