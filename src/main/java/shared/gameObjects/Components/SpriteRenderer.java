package shared.gameObjects.Components;

import java.io.Serializable;
import shared.gameObjects.GameObject;

/** Sprite Rendering Component called on by the game loop to render assets */
public class SpriteRenderer extends Component implements Serializable {
  // INCLUDE: Image or Sprite
  // LAYERS? POSITIONS? MIGHT BE POSSIBLE, PLACED HERE

  public SpriteRenderer(GameObject parent) {
    super(parent, ComponentType.SPRITE_RENDERER);
  }

  @Override
  public void update() {
    // Render Methods go here
  }
}
