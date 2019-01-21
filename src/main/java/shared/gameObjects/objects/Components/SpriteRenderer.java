package shared.gameObjects.objects.Components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.gameObjects.GameObject;

import java.io.Serializable;

/**
 * Sprite Rendering Component called on by the game loop to render assets
 */
public class SpriteRenderer extends Component implements Serializable {
    //INCLUDE: Image or Sprite
    //LAYERS? POSITIONS? MIGHT BE POSSIBLE, PLACED HERE

    public SpriteRenderer(GameObject parent) {
        this.parent = parent;
        type = ComponentType.SPRITE_RENDERER;
    }

    @Override
    public void update() {
        //Render Methods go here
    }


}
