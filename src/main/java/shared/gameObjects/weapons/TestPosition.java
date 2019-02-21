package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class TestPosition extends GameObject {

  private MachineGun gun;
  
  public TestPosition(double x, double y, UUID uuid, MachineGun gun) {
    super(x, y, 10, 10, ObjectID.Bullet, uuid);
    this.gun = gun;
  }
  
  @Override
  public void update() {
    this.setX(gun.getX());
    this.setY(gun.getY());
    
    super.update();
  }
  
  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 20, 20, true, 
        Path.convert("images/weapons/bullet.png"));
  }
  
}
