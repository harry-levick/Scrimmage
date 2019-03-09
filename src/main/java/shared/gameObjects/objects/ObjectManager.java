package shared.gameObjects.objects;

import shared.physics.Physics;

public class ObjectManager {

  public static boolean outlinedBlocks;

  public static float colouredTimer;
  public static final float TIME_BETWEEN_COLOUR_SWAPS = 3.0f;
  public static ColourBlock currentActive;

  public static void update() {
    colouredTimer -= Physics.TIMESTEP;
    if(colouredTimer <= 0) {
      colouredTimer = TIME_BETWEEN_COLOUR_SWAPS;
      switch (currentActive) {
        case RED:
          currentActive = ColourBlock.GREEN;
          break;
        case GREEN:
          currentActive = ColourBlock.BLUE;
          break;
        case BLUE:
          currentActive = ColourBlock.YELLOW;
          break;
        case YELLOW:
          currentActive = ColourBlock.RED;
          break;
      }
    }
  }
}

enum ColourBlock {
  RED, GREEN, BLUE, YELLOW
}
