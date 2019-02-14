package shared.gameObjects.weapons;

import java.util.UUID;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectID;

public class MachineGun extends Gun {

  private static String imagePath = "images/weapons/machineGun.jpg";  // path to Machine Gun image
  
  public MachineGun(
      double x,
      double y,
      double sizeX,
      double sizeY,
      String name,
      UUID uuid) {
    
    super(
        x, 
        y,
        sizeX,
        sizeY, 
        ObjectID.Weapon, // ObjectID 
        5, // damage
        10, // weight
        name, 
        5, // ammo
        1, // bulletSpeed 
        70, // fireRate
        50, // bulletWidth
        true, // fullAutoFire 
        false, // singleHanded
        uuid
    );
    
  }
  
  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      Bullet bullet = new MachineGunBullet(getX(), getY(), 10, 10, mouseX, mouseY, this.bulletWidth, this.bulletSpeed,
          uuid);
      this.currentCooldown = getDefaultCoolDown();
      deductAmmo();
    }
  }
  
  @Override
  public void update() {
    super.update();
  }

  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());
  }

  @Override
  public void interpolatePosition(float alpha) {
    
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    //this.animation.supplyAnimation("default", imagePath);
    this.animation.supplyAnimationWithSize("default", 40, 40, true, imagePath);
  }
}
