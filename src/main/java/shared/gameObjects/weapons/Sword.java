package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;

public class Sword extends Melee {

  //private static String imagePath = "images/weapons/sword.jpg";
  private static String imagePath = "images/weapons/sword3.png";
  private double rotate;
  private double[] angles;
  private int currentAngleIndex;

  /**
   * Constructor of the Sword class
   *
   * @param x The x position of the sword
   * @param y The y position of the sword
   * @param name Name of the sword
   * @param range Range of the sword
   * @param beginAngle The starting angle when the sword swing
   * @param endAngle The ending angle when the sword swing
   * @param uuid The UUID of the sword
   */
  public Sword(double x, double y, double sizeX, double sizeY, String name, UUID uuid) {
    super(
        x,
        y,
        sizeX, 
        sizeY, 
        ObjectID.Weapon,
        20,  // damage
        10,  // weight
        name, 
        30,  // ammo
        60,  // fireRate
        30,  // range
        50,  // beginAngle
        20,  // endAngle
        uuid);
  }
  
  @Override
  public void fire(double mouseX, double mouseY) {
    super.fire(mouseX, mouseY);
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
    
    // set rotation of the sword
    if (this.attacking) {
      this.imageView.setRotate(45 + (-1 * getAngle(currentAngleIndex)));
      // set incrementation of angles for frames
      currentAngleIndex += 4;
      if (currentAngleIndex >= (int)(beginAngle + endAngle + 1)) {
        attacking = false;
        currentAngleIndex = 0;
        this.imageView.setRotate(0);
      }
    }
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 50, 50, true, this.imagePath);
  }
}
