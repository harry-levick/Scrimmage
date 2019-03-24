package shared.physics.types;

/**
 * @author fxa579 Enum used to layer collision boxes
 */
public enum ColliderLayer {
  /**
   * Default layer all Colliders take when not specified a layer
   */
  DEFAULT(0),
  /**
   * The layer the player and its limbs occupy
   */
  PLAYER(1),
  /**
   * Used for general objects, such as blocks
   */
  OBJECT(2),
  /**
   * Used for static objects in a level, like walls and floors or potentially decorative triggers
   */
  PLATFORM(3),
  /**
   * Used exclusively by the particle system; do not use this unless you know what you are doing
   */
  PARTICLE(4),
  /**
   * Used for the contact collider for collectables so that they don't phase through the floor. Use
   * default for the trigger.
   */
  COLLECTABLE(5),
  /**
   * Limbs
   */
  LIMBS(6),
  /**
   * Projectiles such as bullets and grenades that affect gameplay should be on this layer
   */
  PROJECTILE(7);
  int id;

  ColliderLayer(int i) {
    id = i;
  }

  public int toInt() {
    return id;
  }
}
