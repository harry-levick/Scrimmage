package shared.gameObjects.background;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public abstract class Background extends GameObject {

  public Background(UUID objectUUID) {
    super(0, 0, 1920, 1080, ObjectID.Background, objectUUID);

  }

  public void render() {
    super.render();
  }
}
