package server.ai;

import shared.gameObjects.players.Player;

/** @author Harry Levick (hxl799) */
public enum FSA {


  ATTACKING() {
    public FSA next(Player targetPlayer, Bot bot, double distance, double prevX, double prevY) {

      // TODO possibly change this to just getRange() on the weapon?
      double weaponRange = (bot.getHolding().isGun()) ? Double.POSITIVE_INFINITY :
          bot.getHolding().getRange();

      int botHealth = bot.getHealth();
      int enemyHealth = targetPlayer.getHealth();

      if ( (distance > weaponRange) &&
          ((botHealth >= this.highHealth) || (botHealth >= enemyHealth * 1.5)) ) {
        return CHASING;

      } else if ( (distance <= weaponRange) &&
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
    public FSA next(Player targetPlayer, Bot bot, double prevX, double prevY) {
      return null;
    }
  },
  CHASING_ATTACKING() {
    public FSA next(Player targetPlayer, Bot bot, double prevX, double prevY) {
      return null;
    }
  },
  FLEEING() {
    public FSA next(Player targetPlayer, Bot bot, double prevX, double prevY) {
      return null;
    }
  },
  FLEEING_ATTACKING() {
    public FSA next(Player targetPlayer, Bot bot, double prevX, double prevY) {
      return null;
    }
  },
  STILL() {
    public FSA next(Player targetPlayer, Bot bot, double prevX, double prevY) {
      return null;
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FSA next(Player targetPlayer, Bot bot, double prevX, double prevY) {
      return FSA.STILL;
    }
  };

  public abstract FSA next(Player targetPlayer, Bot bot, double prevX, double prevY);
  final int highHealth = 66;
  final int mediumHealth = 33;

  FSA() {

  }


}
