package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

/**
 * @author hlf764
 */
public class Bullet extends GameObject {

  public boolean isHit; // true if there is an object at that position
  private double width; // width of bullet
  private double speed; // speed of bullet
  private double newX; // new x position when update() is called
  private double newY; // new y position when update() is called
  private double slope; // the slope of the bullet path
  private double deltaX; // change in x in every update
  private double deltaY; // change in y in every update

  public Bullet(
      double gunX, // gun initial x position
      double gunY, // gun initial y position
      double sizeX,
      double sizeY,
      double mouseX, // mouse initial x position
      double mouseY, // mouse initial y position
      double width, // the width of the bullet
      double speed, // the speed of the bullet
      UUID uuid) { // uuid of this bullet

    super(gunX, gunY, sizeX, sizeY, ObjectID.Bullet, uuid);
    setWidth(width);
    setSpeed(speed);

    this.newX = gunX;
    this.newY = gunY;
    this.slope = (gunY - mouseY) / (gunX - mouseX); // slope of the bullet path
    // deltaX and deltaY show the change in x and y values in every updates
    // The last bit of the expression shows whether x and y should progress in
    // positive or negative direction
    this.deltaX = (this.speed * Math.cos(Math.atan(slope))) * ((mouseX > gunX) ? 1 : -1);
    this.deltaY = (this.speed * Math.sin(Math.atan(slope))) * ((mouseY > gunY) ? 1 : -1);
    this.isHit = false;

    render();
  }

  public void fire() {
    this.newX += deltaX;
    this.newY += deltaY;
  }

  @Override
  public void update() {
    this.fire();

    // if something is in this position (will take width into account later)
    // isHit = true;
    // apply effect (deduct hp, sound, physics)
    // destroy this object
  }

  @Override
  public void render() {
    super.render();
    imageView.relocate(newX, newY);
  }

  @Override
  public void interpolatePosition(float alpha) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getState() {
    return null;
  }


  public double getWidth() {
    return this.width;
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  public void setWidth(double newWidth) {
    if (newWidth > 0) {
      this.width = newWidth;
    }
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setSpeed(double newSpeed) {
    if (newSpeed > 0) {
      this.speed = newSpeed;
    }
  }
  // -------------------
  // Setters and Getters
  // --------END--------

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/weapons/bullet.png");
  }
}
