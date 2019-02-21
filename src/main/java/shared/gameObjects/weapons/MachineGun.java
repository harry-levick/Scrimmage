package shared.gameObjects.weapons;

import client.handlers.audioHandler.AudioHandler;
import javafx.scene.transform.Rotate;
import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import shared.util.Path;
import shared.util.maths.Vector2;

public class MachineGun extends Gun {

  private static String imagePath = "images/weapons/machinegun.png"; // path to Machine Gun image
  private static String audioPath = "audio/sound-effects/laser_gun.wav"; // path to Machine Gun sfx
  private static double sizeX = 80, sizeY = 20;
  private double[] holderHandPos;
  private Rotate rotate;

  public MachineGun(double x, double y, String name, Player holder,
      UUID uuid) {

    super(
        x,
        y,
        sizeX, // sizeX
        sizeY, // sizeY
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
    
    rotate = new Rotate();
    // pivot = position of the grip
    // If changing the value of this, change the value in all getGrip() methods
    rotate.setPivotX(20);
    rotate.setPivotY(10);
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      double bulletX = holder.getFacingRight() ? getMuzzleX() : getMuzzleFlipX();
      double bulletY = holder.getFacingRight() ? getMuzzleY() : getMuzzleFlipY();
      Bullet bullet =
          new MachineGunBullet(
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
      //new AudioHandler(super.getSettings()).playSFX("CHOOSE_YOUR_CHARACTER");
      new AudioHandler(settings).playSFX("MACHINEGUN");
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
    
    imageView.getTransforms().remove(rotate);
    
    double mouseX = holder.mouseX;
    double mouseY = holder.mouseY;
    Vector2 mouseV = new Vector2((float)mouseX, (float)mouseY);
    Vector2 gripV = new Vector2((float)this.getGripX(), (float)this.getGripY());
    double angle = mouseV.sub(gripV).angle() * 180 / 3.141592654f;  // degree
    //System.out.println("angle="+angle);
    
    // Change the facing of the player when aiming the other way
    if (holder.getFacingRight() && mouseX < this.getGripX())
      holder.setFacingLeft(true);
    else if (holder.getFacingLeft() && mouseX > this.getGripX())
      holder.setFacingRight(true);
    
    // Rotate and translate the image
    if (holder.getFacingLeft()) {
      imageView.setScaleX(-1);
      rotate.setAngle(-angle);
      imageView.getTransforms().add(rotate);
      imageView.setTranslateX(this.getGripFlipX());
      imageView.setTranslateY(this.getGripFlipY());
    } else if (holder.getFacingRight()) {
      imageView.setScaleX(1);
      rotate.setAngle(angle);
      imageView.getTransforms().add(rotate);
      imageView.setTranslateX(this.getGripX());
      imageView.setTranslateY(this.getGripY());
    }
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
  
  // =============================
  // Get Grip and Muzzle positions
  // =============================
  public double getGripX() {
    return holderHandPos[0] - 20;
  }

  public double getGripY() {
    return holderHandPos[1] - 10;
  }

  public double getGripFlipX() {
    return holderHandPos[0] - 55;
  }

  public double getGripFlipY() {
    return holderHandPos[1] - 10;
  }

  public double getMuzzleX() {
    return getGripX() + 68;
  }

  public double getMuzzleY() {
    return getGripY() - 4;
  }

  public double getMuzzleFlipX() {
    return getGripFlipX() - 12;
  }

  public double getMuzzleFlipY() {
    return getGripFlipY() - 8;
  }
}
