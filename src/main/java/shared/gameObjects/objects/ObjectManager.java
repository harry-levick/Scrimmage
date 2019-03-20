package shared.gameObjects.objects;

import shared.physics.Physics;

/**
 * A general class to manage level-wide timers and effects
 */
public class ObjectManager {

  /**
   * The time taken between the colour blocks changing solidity
   */
  public static final float TIME_BETWEEN_COLOUR_SWAPS = 3.0f;
  private static float colouredTimer = 3.0f;
  /**
   * The current active coloured block
   */
  public static ColourBlock currentActive = ColourBlock.RED;

  /**
   * Updates the timers and content of the object manager
   */
  public static void update() {
    colouredTimer -= Physics.TIMESTEP;
    if (colouredTimer <= 0) {
      colouredTimer = TIME_BETWEEN_COLOUR_SWAPS;
      switch (currentActive) {
        case RED:
          currentActive = ColourBlock.BLUE;
          break;
        case BLUE:
          currentActive = ColourBlock.GREEN;
          break;
        case GREEN:
          currentActive = ColourBlock.YELLOW;
          break;
        case YELLOW:
          currentActive = ColourBlock.RED;
          break;
      }
    }
  }
}
