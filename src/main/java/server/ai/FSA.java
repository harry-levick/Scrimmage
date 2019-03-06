package server.ai;

import java.util.ArrayList;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Melee;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * @author Harry Levick (hxl799)
 */
public enum FSA {
  ATTACKING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;

      Vector2 botPos = bot.getTransform().getPos();
      Collision rayCast = Physics.raycast(botPos,
          targetPlayer.getTransform().getPos().sub(botPos));

      boolean inSight = ((Rigidbody) rayCast.getCollidedObject()
          .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;

      if (((newDist > weaponRange) || !inSight)
          && (botHealth >= this.HIGH_HEALTH)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return CHASING;

      } else if ((newDist <= weaponRange)
          && inSight
          && (newDist > prevDist)
          && (botHealth >= this.HIGH_HEALTH)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return CHASING_ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH)
          || ((ammoLeft == 0) && bot.getHolding().isGun())) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH)
          && (botHealth >= this.MEDIUM_HEALTH)
          && (newDist <= prevDist)
          && (newDist <= weaponRange)
          && inSight
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return FLEEING_ATTACKING;

      } else if (inSight) {
        return ATTACKING;
      } else
        return IDLE;
    }
  },
  CHASING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;

      Vector2 botPos = bot.getTransform().getPos();
      Collision rayCast = Physics.raycast(botPos,
          targetPlayer.getTransform().getPos().sub(botPos));

      boolean inSight = ((Rigidbody) rayCast.getCollidedObject()
          .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;

      if ((newDist <= weaponRange)
          && inSight
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())
          && (botHealth >= this.HIGH_HEALTH)) {
        return ATTACKING;

      } else if ((newDist <= weaponRange)
          && inSight
          && (newDist > prevDist)
          && (botHealth >= this.HIGH_HEALTH)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return CHASING_ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH)
          || ((bot.getHolding().isGun()) && (ammoLeft == 0))) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH)
          && inSight
          && (botHealth >= this.MEDIUM_HEALTH)
          && (newDist < prevDist)
          && (newDist <= weaponRange)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return FLEEING_ATTACKING;

      } else if (!inSight) {
        return CHASING;
      } else return IDLE;
    }
  },
  CHASING_ATTACKING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;

      Vector2 botPos = bot.getTransform().getPos();
      Collision rayCast = Physics.raycast(botPos,
          targetPlayer.getTransform().getPos().sub(botPos));

      boolean inSight = ((Rigidbody) rayCast.getCollidedObject()
          .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;

      if ((newDist <= weaponRange)
          && inSight
          &&
          // Target staying relatively still
          (prevDist * 1.05 <= newDist && newDist <= prevDist * 1.05)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())
          && (botHealth >= this.HIGH_HEALTH)) {
        return ATTACKING;

      } else if ((botHealth >= this.HIGH_HEALTH)
          && (newDist > weaponRange || !inSight)
          && (newDist > prevDist)) {
        return CHASING;

      } else if ((botHealth <= this.MEDIUM_HEALTH)
          || ((bot.getHolding().isGun()) && (ammoLeft == 0))) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH)
          && inSight
          && (botHealth >= MEDIUM_HEALTH)
          && (newDist < prevDist)
          && (newDist <= weaponRange)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return FLEEING_ATTACKING;

      } else if (inSight) {
        return CHASING_ATTACKING;
      } else
        return IDLE;
    }
  },
  FLEEING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;

      Vector2 botPos = bot.getTransform().getPos();
      Collision rayCast = Physics.raycast(botPos,
          targetPlayer.getTransform().getPos().sub(botPos));

      boolean inSight = ((Rigidbody) rayCast.getCollidedObject()
          .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;

      Melee temp;

      double enemyWeaponRange =
          (targetPlayer.getHolding().isGun())
              ? Double.POSITIVE_INFINITY
              : (temp = (Melee) targetPlayer.getHolding()).getRange();

      if ((newDist <= weaponRange)
          && inSight
          && (botHealth >= this.HIGH_HEALTH)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return ATTACKING;

      } else if ((botHealth >= this.HIGH_HEALTH)
          && (newDist > prevDist)
          && (newDist > weaponRange || !inSight)) {
        return CHASING;

      } else if ((botHealth >= this.HIGH_HEALTH)
          && (newDist > prevDist)
          && (newDist <= weaponRange)
          && inSight
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return CHASING_ATTACKING;

      } else if ((newDist <= weaponRange)
          && inSight
          && (newDist < prevDist)
          && (botHealth <= this.HIGH_HEALTH)
          && (botHealth >= this.MEDIUM_HEALTH)
          && (((ammoLeft > 0) && bot.getHolding().isGun()) || bot.getHolding().isMelee())) {
        return FLEEING_ATTACKING;
        // If we have run out of the range of the enemy
      } else if ((newDist > enemyWeaponRange) && (botHealth >= this.HIGH_HEALTH)) {
        return IDLE;

      } else {
        return FLEEING;
      }
    }
  },
  FLEEING_ATTACKING() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;

      Vector2 botPos = bot.getTransform().getPos();
      Collision rayCast = Physics.raycast(botPos,
          targetPlayer.getTransform().getPos().sub(botPos));

      boolean inSight = ((Rigidbody) rayCast.getCollidedObject()
          .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;

      if (((botHealth >= this.HIGH_HEALTH))
          && (newDist < prevDist)
          && (newDist <= weaponRange)
          && inSight
          && ((bot.getHolding().isGun() && ammoLeft > 0) || bot.getHolding().isMelee())) {
        return ATTACKING;

      } else if (((botHealth >= this.HIGH_HEALTH)) && (newDist > weaponRange || !inSight)) {
        return CHASING;

      } else if (((botHealth >= this.HIGH_HEALTH))
          && (newDist <= weaponRange)
          && inSight
          && (newDist > prevDist)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return CHASING_ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH) && (prevDist > newDist)) {
        return FLEEING;

      } else if (inSight) {
        return FLEEING_ATTACKING;
      } else
        return IDLE;
    }
  },
  IDLE() {
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;

      Vector2 botPos = bot.getTransform().getPos();
      Collision rayCast = Physics.raycast(botPos,
          targetPlayer.getTransform().getPos().sub(botPos));

      boolean inSight = ((Rigidbody) rayCast.getCollidedObject()
          .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;

      if (((botHealth >= this.HIGH_HEALTH))
          && inSight
          && (newDist <= weaponRange)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return ATTACKING;

      } else if (((botHealth >= this.HIGH_HEALTH)) && (newDist > weaponRange || !inSight)) {
        return CHASING;

      } else if (((botHealth >= this.HIGH_HEALTH))
          && (newDist <= weaponRange)
          && inSight
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())
          && (newDist > prevDist)) {
        return CHASING_ATTACKING;

      } else if ((botHealth < this.MEDIUM_HEALTH)) {
        return FLEEING;

      } else if ((botHealth <= this.HIGH_HEALTH)
          && inSight
          && (botHealth >= this.MEDIUM_HEALTH)
          && (newDist <= weaponRange)
          && (newDist < prevDist)) {
        return FLEEING_ATTACKING;

      } else {
        return IDLE;
      }
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the still state.
    public FSA next(Player targetPlayer, Player bot, double prevDist, double newDist) {
      return FSA.IDLE;
    }
  };

  final int HIGH_HEALTH = 66;
  final int MEDIUM_HEALTH = 33;

  FSA() {
  }

  public abstract FSA next(Player targetPlayer, Player bot, double prevDist, double newDist);
}
