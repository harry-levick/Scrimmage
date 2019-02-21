package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class MachineGun extends Gun {

  private static String imagePath = "images/weapons/machinegun.png"; // path to Machine Gun image
  private double[] holderHandPos;

  public MachineGun(double x, double y, String name, Player holder,
      UUID uuid) {

    super(
        x,
        y,
        80,
        20,
        ObjectID.Weapon, // ObjectID
        5, // damage
        10, // weight
        name,
        50, // ammo
        50, // bulletSpeed
        70, // fireRate
        50, // bulletWidth
        holder,
        true, // fullAutoFire
        false, // singleHanded
        uuid);
    holderHandPos = holder.getHandPos();
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      double bulletX = holderHandPos[0] + (holder.getFacingRight()? getMuzzleX() : getMuzzleFlipX());
      double bulletY = holderHandPos[1] + (holder.getFacingRight()? getMuzzleY() : getMuzzleFlipY()); 
      Bullet bullet =
          new MachineGunBullet(
              //getX() + (holder.getFacingRight()? getMuzzleX() : getMuzzleFlipX()),
              //getY() + (holder.getFacingRight()? getMuzzleY() : getMuzzleFlipY()),
              bulletX,
              bulletY,
              mouseX,
              mouseY,
              this.bulletWidth,
              this.bulletSpeed,
              this.damage,
              this.holder,
              uuid);
      this.currentCooldown = getDefaultCoolDown();
      deductAmmo();
    }
  }

  @Override
  public void update() {
    super.update();
    holderHandPos = holder.getHandPos();
  }

  @Override
  public void render() {
    super.render();
    /*
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());
    */
    /*
    if (holder.getFacingLeft()) {
      imageView.setScaleX(-1);
      imageView.setTranslateX(holderHandPos[0] + this.getGripFlipX());
      imageView.setTranslateY(holderHandPos[1] + this.getGripFlipY());
    }
    else if (holder.getFacingRight()) {
      imageView.setScaleX(1);
      imageView.setTranslateX(holderHandPos[0] + this.getGripX());
      imageView.setTranslateY(holderHandPos[1] + this.getGripY());
    }
    */
    
    System.out.println(String.format("(%fx,%fy) (%fgx,%fgy) (%fhx,%fhy)", this.getX(), this.getY(), this.getGripX(), this.getGripY(), this.holderHandPos[0], this.holderHandPos[1]));
    imageView.setTranslateX(this.getGripX());
    imageView.setTranslateY(this.getGripY());
    
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
    this.animation.supplyAnimationWithSize("default", 40, 40, true, Path.convert(this.imagePath));
  }
  
  public double getGripX() {
    return holderHandPos[0] - 20;
  }
  
  public double getGripY() {
    return holderHandPos[1] - 8;
  }
  
  public double getGripFlipX() {
    return -20;
  }
  
  public double getGripFlipY() {
    return -10;
  }
  
  public double getMuzzleX() {
    return 54;
  }
  
  public double getMuzzleY() {
    return 6;
  }
  
  public double getMuzzleFlipX() {
    return -80;
  }
  
  public double getMuzzleFlipY() {
    return -10;
  }
}
