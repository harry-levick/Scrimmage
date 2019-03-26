package client.handlers.effectsHandler;

/**
 * Emitter Types
 */
public enum ParticleType {
  /**
   * Emits particle from a line
   */
  LINE,
  /**
   * Emits particles in a circle going away from the centre
   */
  OUTWARDS,
  /**
   * Emits particles in a circle going towards the centre
   */
  INWARDS,
}
