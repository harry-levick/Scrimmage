package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

/**
 * @author Henry Fung (hlf764)
 *
 */
public class HandgunBullet extends Bullet {
  
  private static String imagePath = "images/weapons/bullet.png";
  private Rigidbody rb;
  
  public HandgunBullet(
      double gunX,
      double gunY,
      double sizeX,
      double sizeY,
      double mouseX,
      double mouseY,
      double width,
      double speed,
      UUID uuid) {
    
    super(gunX, gunY, sizeX, sizeY, mouseX, mouseY, width, speed, uuid);
  }
  
  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", this.imagePath);
  }

  @Override
  public void setRigitBody(float gravityScale) {
    this.rb = new Rigidbody(RigidbodyType.DYNAMIC, 100f, gravityScale, 0.1f, new MaterialProperty(0, 0, 0), new AngularData(0, 0, 0, 0), this);
  }
}