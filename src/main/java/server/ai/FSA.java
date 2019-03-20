package server.ai;

import shared.gameObjects.Blocks.Wood.WoodBlockLargeObject;
import shared.gameObjects.Blocks.Wood.WoodBlockSmallObject;
import shared.gameObjects.Blocks.Wood.WoodFloorObject;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Gun;
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
    public FSA next(Player targetPlayer, Player bot, double targetDistance) {
      if (targetPlayer == null)
        return IDLE;

      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      boolean inSight;

      Vector2 botPos = bot.getTransform().getPos();
      Vector2 botPosCenter = botPos.add(bot.getTransform().getSize().mult(0.5f));
      Vector2 enemyPos = targetPlayer.getTransform().getPos();
      Vector2 enemyPosCenter = enemyPos.add(bot.getTransform().getSize().mult(0.5f));

      // Use the worldScene of the path finding to raycast, instead of the actual gameObjects list.
      Collision rayCast = Physics.raycastAi(botPosCenter,
          enemyPosCenter.sub(botPosCenter),
          null,
          (Bot) bot,
          false);

      if (bot.getHolding().isGun() && ((Gun) bot.getHolding()).firesExplosive()) {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;
      } else {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC &&
            !(rayCast.getCollidedObject() instanceof WoodBlockSmallObject ||
            rayCast.getCollidedObject() instanceof WoodBlockLargeObject ||
            rayCast.getCollidedObject() instanceof WoodFloorObject);
      }

      if (((targetDistance > weaponRange) || !inSight)
          && (botHealth >= this.HIGH_HEALTH)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return CHASING;

      } else if ((botHealth <= this.MEDIUM_HEALTH)
          || ((ammoLeft == 0) && bot.getHolding().isGun())) {
        return FLEEING;

      } else if (inSight) {
        return ATTACKING;

      } else
        return IDLE;
    }
  },
  CHASING() {
    public FSA next(Player targetPlayer, Player bot, double targetDistance) {
      if (targetPlayer == null)
        return IDLE;

      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      boolean inSight;

      Vector2 botPos = bot.getTransform().getPos();
      Vector2 botPosCenter = botPos.add(bot.getTransform().getSize().mult(0.5f));
      Vector2 enemyPos = targetPlayer.getTransform().getPos();
      Vector2 enemyPosCenter = enemyPos.add(bot.getTransform().getSize().mult(0.5f));

      // Use the worldScene of the path finding to raycast, instead of the actual gameObjects list.
      Collision rayCast = Physics.raycastAi(botPosCenter,
          enemyPosCenter.sub(botPosCenter),
          null,
          (Bot) bot,
          false);


      if (bot.getHolding().isGun() && ((Gun) bot.getHolding()).firesExplosive()) {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;
      } else {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC &&
            !(rayCast.getCollidedObject() instanceof WoodBlockSmallObject ||
                rayCast.getCollidedObject() instanceof WoodBlockLargeObject ||
                rayCast.getCollidedObject() instanceof WoodFloorObject);
      }

      if ((targetDistance <= weaponRange)
          && inSight
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())
          && (botHealth >= this.HIGH_HEALTH)) {
        return ATTACKING;

      } else if ((botHealth <= this.MEDIUM_HEALTH)
          || ((bot.getHolding().isGun()) && (ammoLeft == 0))) {
        return FLEEING;

      } else if (!inSight) {
        return CHASING;

      } else {
        return IDLE;
      }
    }
  },
  FLEEING() {
    public FSA next(Player targetPlayer, Player bot, double targetDistance) {
      if (targetPlayer == null)
        return IDLE;

      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      boolean inSight;

      Vector2 botPos = bot.getTransform().getPos();
      Vector2 botPosCenter = botPos.add(bot.getTransform().getSize().mult(0.5f));
      Vector2 enemyPos = targetPlayer.getTransform().getPos();
      Vector2 enemyPosCenter = enemyPos.add(bot.getTransform().getSize().mult(0.5f));

      // Use the worldScene of the path finding to raycast, instead of the actual gameObjects list.
      Collision rayCast = Physics.raycastAi(botPosCenter,
          enemyPosCenter.sub(botPosCenter),
          null,
          (Bot) bot,
          false);

      if (bot.getHolding().isGun() && ((Gun) bot.getHolding()).firesExplosive()) {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;
      } else {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC &&
            !(rayCast.getCollidedObject() instanceof WoodBlockSmallObject ||
                rayCast.getCollidedObject() instanceof WoodBlockLargeObject ||
                rayCast.getCollidedObject() instanceof WoodFloorObject);
      }

      Melee temp;

      double enemyWeaponRange =
          (targetPlayer.getHolding().isGun())
              ? Double.POSITIVE_INFINITY
              : (temp = (Melee) targetPlayer.getHolding()).getRange();

      if ((targetDistance <= weaponRange)
          && inSight
          && (botHealth >= this.HIGH_HEALTH)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return ATTACKING;

      } else if ((botHealth >= this.HIGH_HEALTH)
          && (targetDistance > weaponRange || !inSight)) {
        return CHASING;

        // If we have run out of the range of the enemy
      } else if ((targetDistance > enemyWeaponRange) && (botHealth >= this.HIGH_HEALTH)) {
        return IDLE;

      } else {
        return FLEEING;
      }
    }
  },
  IDLE() {
    public FSA next(Player targetPlayer, Player bot, double targetDistance) {
      if (targetPlayer == null)
        return IDLE;

      StateInfo.setInfo(targetPlayer, bot);

      double weaponRange = StateInfo.weaponRange;
      int ammoLeft = StateInfo.ammoLeft;
      int botHealth = StateInfo.botHealth;
      boolean inSight;

      Vector2 botPos = bot.getTransform().getPos();
      Vector2 botPosCenter = botPos.add(bot.getTransform().getSize().mult(0.5f));
      Vector2 enemyPos = targetPlayer.getTransform().getPos();
      Vector2 enemyPosCenter = enemyPos.add(bot.getTransform().getSize().mult(0.5f));

      // Use the worldScene of the path finding to raycast, instead of the actual gameObjects list.
      Collision rayCast = Physics.raycastAi(botPosCenter,
          enemyPosCenter.sub(botPosCenter),
          null,
          (Bot) bot,
          false);

      if (bot.getHolding().isGun() && ((Gun) bot.getHolding()).firesExplosive()) {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC;
      } else {
        inSight = ((Rigidbody) rayCast.getCollidedObject()
            .getComponent(ComponentType.RIGIDBODY)).getBodyType() != RigidbodyType.STATIC &&
            !(rayCast.getCollidedObject() instanceof WoodBlockSmallObject ||
                rayCast.getCollidedObject() instanceof WoodBlockLargeObject ||
                rayCast.getCollidedObject() instanceof WoodFloorObject);
      }

      if (((botHealth >= this.HIGH_HEALTH))
          && inSight
          && (targetDistance <= weaponRange)
          && ((ammoLeft > 0) && bot.getHolding().isGun() || bot.getHolding().isMelee())) {
        return ATTACKING;

      } else if (((botHealth >= this.HIGH_HEALTH)) && (targetDistance > weaponRange || !inSight)) {
        return CHASING;

      } else if ((botHealth < this.MEDIUM_HEALTH)) {
        return FLEEING;

      } else {
        return IDLE;
      }
    }
  },
  INITIAL_STATE() {
    // The initial state just acts as an entry point, and so directs straight to the IDLE state.
    public FSA next(Player targetPlayer, Player bot, double targetDistance) {
      return FSA.IDLE;
    }
  };

  final int HIGH_HEALTH = 66;
  final int MEDIUM_HEALTH = 33;

  FSA() {
  }

  /**
   * Determines the state to update to.
   * @param targetPlayer The bots target.
   * @param bot The bot that this FSA is associated with.
   * @param targetDistance The distance to the target.
   * @return The state that we should update to.
   */
  public abstract FSA next(Player targetPlayer, Player bot, double targetDistance);
}
