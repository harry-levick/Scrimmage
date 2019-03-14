package shared.gameObjects.weapons;

import java.util.UUID;
import javafx.scene.transform.Translate;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class Sword extends Melee {

  // private static String imagePath = "images/weapons/sword.jpg";
  private static String imagePath = "images/weapons/sword1.png";
  private int currentAngleIndex;
  private Translate translate;
  private double attackAngleSign; // -1 if facingLeft, 1 if facingRight

  /**
   * Constructor of the Sword class
   *
   * @param x The x position of the sword
   * @param y The y position of the sword
   * @param name Name of the sword
   * @param uuid The UUID of the sword
   */
  public Sword(double x, double y, String name, Player holder, UUID uuid) {
    super(
        x,
        y,
        50,
        50,
        ObjectType.Weapon,
        20, // hazard
        10, // weight
        name,
        30, // ammo
        60, // fireRate
        holder,
        50, // range
        50, // beginAngle
        20, // endAngle
        uuid);

    translate = new Translate();
    attackAngleSign = 1;
  }

  public Sword(Sword that) {
    this(that.getX(), that.getY(), that.name, that.holder, UUID.randomUUID());
  }

  @Override
  public void fire(double mouseX, double mouseY) {
    super.fire(mouseX, mouseY);
  }

  @Override
  public void render() {
    super.render();

    /*
    imageView.getTransforms().remove(translate);
    translate.transform(this.getX(), this.getY());
    //imageView.setTranslateX(this.getX());
    //imageView.setTranslateY(this.getY());
    imageView.getTransforms().add(translate);
    System.out.println(imageView.getTranslateX()+" "+imageView.getTranslateY());
    */

    // set rotation of the sword
    /*
    if (this.attacking) {
      this.imageView.setRotate(45 + (-1 * getAngle(currentAngleIndex)));
      // set incrementation of angles for frames
      currentAngleIndex += 4;
      if (currentAngleIndex >= (int) (beginAngle + endAngle + 1)) {
        attacking = false;
        currentAngleIndex = 0;
        this.imageView.setRotate(0);
      }
    }
    */
    if (this.attacking) {
      this.imageView
          .setRotate((45 * attackAngleSign) + (attackAngleSign * -1 * getAngle(currentAngleIndex)));
      // set incrementation of angles for frames
      currentAngleIndex += 4;
      if (currentAngleIndex >= (int) (beginAngle + endAngle + 1)) {
        attacking = false;
        currentAngleIndex = 0;
        this.imageView.setRotate(0);
      }
    }
  }


  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize(
        "default", this.range, this.range, true, Path.convert(this.imagePath));
  }

  @Override
  public double getGripX() {
    if (holder.isAimingLeft()) {
      return getGripFlipX();
    }

    this.imageView.setScaleX(1);
    if (!attacking) {
      attackAngleSign = 1;
    }
    return holderHandPos[0] - 6;
  }

  @Override
  public double getGripY() {
    if (holder.isAimingLeft()) {
      return getGripFlipY();
    }

    this.imageView.setScaleX(1);
    if (!attacking) {
      attackAngleSign = 1;
    }
    return holderHandPos[1] - 34;
  }

  @Override
  public double getGripFlipX() {
    this.imageView.setScaleX(-1);
    if (!attacking) {
      attackAngleSign = -1;
    }
    return holderHandPos[0] - 34;
  }

  @Override
  public double getGripFlipY() {
    this.imageView.setScaleX(-1);
    if (!attacking) {
      attackAngleSign = -1;
    }
    return holderHandPos[1] - 38;
  }
}
