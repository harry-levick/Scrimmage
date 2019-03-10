package shared.gameObjects.objects;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.types.ColliderLayer;

public abstract class ColouredBlock extends GameObject {

  protected ColourBlock blockType;
  protected BoxCollider bc;

  public ColouredBlock(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, id, exampleUUID);
    addComponent(new Rigidbody(0, this));
    bc = new BoxCollider(this, ColliderLayer.PLATFORM, false);
    addComponent(bc);
  }

  // Initialise the animation
  public void initialiseAnimation() {}

  @Override
  public void update() {
    super.update();
    if (ObjectManager.currentActive == this.blockType) {
      bc.setLayer(ColliderLayer.PLATFORM);
      this.animation.switchAnimation("active");
    } else {
      bc.setLayer(ColliderLayer.PARTICLE);
      this.animation.switchDefault();
    }
  }
}
