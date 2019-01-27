package server.ai;

import javax.xml.bind.attachment.AttachmentMarshaller;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Melee;

/** @author Harry Levick (hxl799) */
public enum FSA {


  ATTACKING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      int enemyHealth = StateInfo.enemyHealth;

      if ((newDist > weaponRange) &&
          ((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) ) {
        return CHASING;

      } else if ((newDist <= weaponRange) &&
          (newDist > prevDist) &&
          ((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) &&
          (ammoLeft > 0)) {
        return CHASING_ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH) ||
          (bot.getHolding().isGun()) && (ammoLeft == 0)) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH) &&
          (botHealth >= this.MEDIUM_HEALTH) &&
          (newDist <= prevDist) &&
          (newDist <= weaponRange) &&
          (ammoLeft > 0)){
        return FLEEING_ATTACKING;

      } else return ATTACKING;
    }

  },
  CHASING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      int enemyHealth = StateInfo.enemyHealth;

      if ((newDist <= weaponRange) &&
          (ammoLeft >= 0) &&
          (botHealth >= this.MEDIUM_HEALTH) &&
          // Target staying relatively still
          (prevDist * 1.05 <= newDist && newDist <= prevDist * 1.05 )) {
        return ATTACKING;

      } else if ((newDist <= weaponRange) &&
          (newDist > prevDist) &&
          ((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) &&
          (ammoLeft > 0)) {
        return CHASING_ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH) ||
          ((bot.getHolding().isGun()) && (ammoLeft == 0) || newDist > weaponRange)) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH) &&
          (botHealth >= this.MEDIUM_HEALTH) &&
          (newDist <= prevDist) &&
          (newDist <= weaponRange) &&
          (ammoLeft > 0)) {
        return FLEEING_ATTACKING;

      } else return CHASING;

    }
  },
  CHASING_ATTACKING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      int enemyHealth = StateInfo.enemyHealth;

      if ((newDist <= weaponRange) &&
          // Target staying relatively still
          (prevDist * 1.05 <= newDist && newDist <= prevDist * 1.05 ) &&
          (ammoLeft >= 0) &&
          ((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) {
        return ATTACKING;

      } else if (((botHealth >= this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) &&
          (newDist > weaponRange) &&
          (newDist > prevDist)) {
        return CHASING;

      } else if ((botHealth <= this.MEDIUM_HEALTH) ||
          ((bot.getHolding().isGun()) && (ammoLeft == 0) || newDist > weaponRange)) {
        return FLEEING;

      } else if ((botHealth < this.HIGH_HEALTH) &&
          (newDist < prevDist) &&
          (newDist <= weaponRange) &&
          (ammoLeft > 0)) {
        return FLEEING_ATTACKING;

      } else return CHASING_ATTACKING;
    }
  },
  FLEEING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      int enemyHealth = StateInfo.enemyHealth;

      Melee temp;

      double enemyWeaponRange = (targetPlayer.getHolding().isGun()) ? Double.POSITIVE_INFINITY :
          (temp = (Melee) targetPlayer.getHolding()).getRange();

      if ((newDist <= weaponRange) &&
          (newDist < prevDist) &&
          (botHealth >= this.HIGH_HEALTH) &&
          (ammoLeft > 0)) {
        return ATTACKING;

      } else if ((botHealth >= this.HIGH_HEALTH) &&
          (newDist > prevDist) &&
          (newDist > weaponRange)) {
        return CHASING;

      } else if ((botHealth >= this.HIGH_HEALTH) &&
          (newDist > prevDist) &&
          (newDist < weaponRange) &&
          (ammoLeft > 0)) {
        return CHASING_ATTACKING;

      } else if ((newDist <= weaponRange) &&
          (newDist < prevDist) &&
          (botHealth <= this.HIGH_HEALTH) &&
          (ammoLeft > 0)) {
        return FLEEING_ATTACKING;
      // If we have run out of the range of the enemy
      } else if (newDist > enemyWeaponRange) {
        return STILL;

      } else return FLEEING;
    }
  },
  FLEEING_ATTACKING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      int enemyHealth = StateInfo.enemyHealth;

      if (((botHealth > this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) &&
          (newDist < prevDist) &&
          ((bot.getHolding().isGun() && ammoLeft > 0) || (newDist <= weaponRange))) {
        return ATTACKING;

      } else if (((botHealth > this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) &&
          (newDist > weaponRange)) {
        return CHASING;

      } else if (((botHealth > this.HIGH_HEALTH) || (botHealth >= enemyHealth * 1.5)) &&
          (newDist <= weaponRange) &&
          (newDist > prevDist)) {
        return CHASING_ATTACKING;

      } else if ((botHealth < this.MEDIUM_HEALTH) &&
          (prevDist > newDist)) {
        return FLEEING;

      } else return FLEEING_ATTACKING;
    }
  },
  STILL() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      int enemyHealth = StateInfo.enemyHealth;

      return null;
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      return FSA.STILL;
    }
  };

  public abstract FSA next(Player targetPlayer, Player bot, double prevDist, double newDist);

  private Object[] retrieveInfo(Player target, Player bot) {
    Object[] info = new Object[5];

    return info;
  }

  final int HIGH_HEALTH = 66;
  final int MEDIUM_HEALTH = 33;

  FSA() {

  }


}