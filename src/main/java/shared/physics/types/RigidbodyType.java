package shared.physics.types;

/**
 * Enum container for Rigidbody types
 */
public enum RigidbodyType {
  /**
   * Dynamic Rigidbodies are updated constantly and moved around by collisions and other external forces
   */
  DYNAMIC,
  /**
   * Static Rigidbodies are not updated and act as infinite mass objects for Dynamic Rigidbodies
   */
  STATIC,
}
