package shared.gameObjects.powerup;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Player;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.ColliderType;
import shared.physics.types.RigidbodyType;

public abstract class Powerup extends GameObject {

  private BoxCollider bcCol;
  private BoxCollider bcTrig;
  private Rigidbody rb;

  public Powerup(
      double x,
      double y,
      double sizeX,
      double sizeY,
      String name,
      UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectType.Powerup, uuid);

    bcCol = new BoxCollider(this, ColliderLayer.COLLECTABLE, false);
    bcTrig = new BoxCollider(this, ColliderLayer.DEFAULT, true);
    rb = new Rigidbody(
        RigidbodyType.DYNAMIC,
        1f,
        1f,
        0.1f,
        new MaterialProperty(0.1f,1,1),
        new AngularData(0, 0, 0, 0),
        this);
    addComponent(bcCol);
    addComponent(bcTrig);
    addComponent(rb);
  }

  public abstract void apply(Player p);

  /**
   * Remove this powerup
   */
  protected void done() {
    this.removeComponent(bcCol);
    this.removeComponent(bcTrig);
    this.removeComponent(rb);
    settings.getLevelHandler().removeGameObject(this);
  }

  @Override
  public void OnTriggerEnter (Collision col) {
    GameObject g = col.getCollidedObject();
    if (g != null && g.getId() == ObjectType.Player) {
      Player p = (Player) g;
      apply(p);
      done();
    }
  }
}
