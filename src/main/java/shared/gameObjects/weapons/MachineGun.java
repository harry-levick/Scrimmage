package shared.gameObjects.weapons;

import client.handlers.audioHandler.AudioHandler;
import client.main.Client;
import java.util.UUID;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.Path;
import shared.util.maths.Vector2;

public class MachineGun extends Gun {

  private static String imagePath = "images/weapons/machinegun.png"; // path to Machine Gun image
  private static String audioPath = "audio/sound-effects/laser_gun.wav"; // path to Machine Gun sfx
  private static float PI = 3.141592654f;
  private static double sizeX = 80, sizeY = 20;

  private double[] holderHandPos;
  private double angleGun; // angle of gun (hand and mouse vs x-axis) (radian)
  private Rotate rotate; // rotate property of gun wrt grip
  private Scale scale;   // scale the image to mirror it


  public MachineGun(double x, double y, String name, Player holder, UUID uuid) {

    super(
        x,
        y,
        sizeX, // sizeX
        sizeY, // sizeY
        ObjectType.Weapon, // ObjectType
        5, // damage
        10, // weight
        name,
        50, // ammo
        50, // bulletSpeed
        70, // fireRate
        12, // bulletWidth
        holder,
        true, // fullAutoFire
        false, // singleHanded
        uuid);

    rotate = new Rotate();
    // pivot = position of the grip
    // If changing the value of this, change the value in all getGrip() methods
    rotate.setPivotX(20);
    rotate.setPivotY(10);

    scale = new Scale();
    scale.setX(-1);
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    if (canFire()) {
      UUID uuid = UUID.randomUUID();
      // double bulletX     = getGripX() + 68 * Math.cos(angleGun) - 4 * Math.sin(angleGun);
      // double bulletY     = getGripY() + 68 * Math.sin(angleGun) - 4 * Math.cos(angleGun);
      double bulletX = getMuzzleX() - 68 + 68 * Math.cos(-angleGun);
      double bulletY = getMuzzleY() - 68 * Math.sin(-angleGun);
      double bulletFlipX = getMuzzleFlipX() + 68 - 68 * Math.cos(angleGun);
      double bulletFlipY = getMuzzleFlipY() - 68 * Math.sin(angleGun);
      Bullet bullet =
          new FireBullet(
              (holder.getFacingRight() ? bulletX : bulletFlipX),
              (holder.getFacingRight() ? bulletY : bulletFlipY),
              mouseX,
              mouseY,
              this.holder,
              uuid);
      this.currentCooldown = getDefaultCoolDown();
      // new AudioHandler(super.getSettings()).playSFX("CHOOSE_YOUR_CHARACTER");
      new AudioHandler(settings, Client.musicActive).playSFX("MACHINEGUN");
      deductAmmo();
    }
  }

  @Override
  public void update() {
    super.update();
    holderHandPos = getHolderHandPos();
  }

  @Override
  public void render() {
    super.render();

    if (holder != null) {
      imageView.getTransforms().clear();

      double mouseX = holder.mouseX;
      double mouseY = holder.mouseY;
      Vector2 mouseV = new Vector2((float) mouseX, (float) mouseY);
      Vector2 gripV = new Vector2((float) this.getGripX(), (float) this.getGripY());
      Vector2 mouseSubGrip = mouseV.sub(gripV);
      angleGun = mouseSubGrip.normalize().angleBetween(Vector2.Zero());  // radian
      double angle = angleGun * 180 / PI;  // degree

      // Change the facing of the player when aiming the other way
      double angleHorizontal;  // degree
      if (holder.getFacingRight()) {
        angleHorizontal = (mouseSubGrip.angleBetween(Vector2.Right())) * 180 / PI;
        if (angleHorizontal > 110f) {
          holder.setFacingLeft(true);
          angle = 180f - angleHorizontal;
        }
        if (angleHorizontal > 90f) {
          angle = angleHorizontal * (mouseY > this.getGripY() ? 1 : -1);
        }
      } else {  // holder facing Left
        angleHorizontal = (mouseSubGrip.angleBetween(Vector2.Left())) * 180 / PI;
        if (angleHorizontal > 110f) {
          holder.setFacingRight(true);
          angle = angleHorizontal - 180f;
        }
        if (angleHorizontal > 90f) {
          angle = angleHorizontal * (mouseY > this.getGripY() ? -1 : 1);
        }
      }

      angleGun = angle * PI / 180;

      // Rotate and translate the image
      if (holder.getFacingLeft()) {
        imageView.setScaleX(-1);
        rotate.setAngle(-angle);
        imageView.getTransforms().add(rotate);
        imageView.setTranslateX(this.getGripFlipX());
        imageView.setTranslateY(this.getGripFlipY());

        holder.setHandRightX(this.getForeGripFlipX());
        holder.setHandRightY(this.getForeGripFlipY());
      } else if (holder.getFacingRight()) {
        imageView.setScaleX(1);
        rotate.setAngle(angle);
        imageView.getTransforms().add(rotate);
        imageView.setTranslateX(this.getGripX());
        imageView.setTranslateY(this.getGripY());

        holder.setHandLeftX(this.getForeGripX());
        holder.setHandLeftY(this.getForeGripY());
      }
    }
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 40, 40, true, Path.convert(this.imagePath));
  }

  public double getForeGripX() {
    if (holder.getFacingLeft())
      return getForeGripFlipX();
    return getGripX() + 50 * Math.cos(-angleGun);
  }

  public double getForeGripY() {
    if (holder.getFacingLeft())
      return getForeGripFlipY();
    return getGripY() + 50 * Math.sin(angleGun);
  }

  public double getForeGripFlipX() {
    return getGripX() + 50 - 30 * Math.cos(angleGun);
  }

  public double getForeGripFlipY() {
    return getGripY() - 50 * Math.sin(angleGun);
  }

  // =============================
  // Get Grip and Muzzle positions
  // =============================
  public double getGripX() {
    if (holder.getFacingLeft()) {
      return getGripFlipX();
    } else {
      return holderHandPos[0] - 20;
    }
  }

  public double getGripY() {
    if (holder.getFacingLeft()) {
      return getGripFlipY();
    } else {
      return holderHandPos[1] - 10;
    }
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
