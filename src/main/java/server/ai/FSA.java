package server.ai;

import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Melee;

/** @author Harry Levick (hxl799) */
public enum FSA {


  ATTACKING() {
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      Melee tmp;
      double weaponRange = (bot.getHolding().isGun()) ? Double.POSITIVE_INFINITY :
          (tmp = (Melee) bot.getHolding()).getRange();
      int ammoLeft = (bot.getHolding().isGun()) ? bot.getHolding().getAmmo() : 0;

      int botHealth = bot.getHealth();
      int enemyHealth = targetPlayer.getHealth();

      if ((newDist > weaponRange) &&
          ((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) ) {
        return CHASING;

      } else if ((newDist <= weaponRange) &&
          (newDist > prevDist) &&
          ((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5))) {
        return CHASING_ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH) ||
          (bot.getHolding().isGun()) && (ammoLeft == 0)) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH) &&
          (botHealth >= this.MEDIUM_HEALTH) &&
          (newDist <= prevDist) &&
          (newDist <= weaponRange)){
        return FLEEING_ATTACKING;

      } else return ATTACKING;
    }

  },
  CHASING() {
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      return null;
    }
  },
  CHASING_ATTACKING() {
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      return null;
    }
  },
  FLEEING() {
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      return null;
    }
  },
  FLEEING_ATTACKING() {
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      return null;
    }
  },
  STILL() {
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      return null;
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist) {
      return FSA.STILL;
    }
  };

  public abstract FSA next(Player targetPlayer, Bot bot, double prevDist, double newDist);
  final int HIGH_HEALTH = 66;
  final int MEDIUM_HEALTH = 33;

  FSA() {

  }


}
