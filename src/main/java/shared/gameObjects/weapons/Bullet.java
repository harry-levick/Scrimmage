package shared.gameObjects.weapons;

import javafx.scene.image.Image;

import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

/**
 * @author hlf764
 */
public class Bullet extends GameObject {
  
  private double width;     // width of bullet
  private double speed;     // speed of bullet
  
  private double newX;      // new x position when update() is called
  private double newY;      // new y position when update() is called
  private double slope;     // the slope of the bullet path
  private double deltaX;    // change in x in every update
  private double deltaY;    // change in y in every update
  private Image bulletImage;// image of the bullet
  public boolean isHit;     // true if there is an object at that position
  
  public Bullet(
      double gunX,          // gun initial x position
      double gunY,          // gun initial y position
      ObjectID id,          // ObjectID
      double mouseX,        // mouse initial x position
      double mouseY,        // mouse initial y position
      double width,         // the width of the bullet
      double speed) {       // the speed of the bullet
    
    super(gunX, gunY, id, "path/to/bulletImage");
    setWidth(width);
    setSpeed(speed);
    
    this.newX = gunX;
    this.newY = gunY;
    this.slope = (gunY - mouseY) / (gunX - mouseX);     // slope of the bullet path
    // deltaX and deltaY show the change in x and y values in every updates
    // The last bit of the expression shows whether x and y should progress in
    // positive or negative direction
    this.deltaX = (this.speed * Math.cos(Math.atan(slope))) * ((mouseX > gunX)? 1 : -1);
    this.deltaY = (this.speed * Math.sin(Math.atan(slope))) * ((mouseY > gunY)? 1 : -1);
    this.bulletImage = getImage();
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
    imageView.relocate(newX, newY);
    if (animate) {
      imageView.setImage(bulletImage);
    }
  }
  
  public Image getImage() {
    // generate a bullet image based on bulletWidth
    return null;
  }
  
  // -------START-------
  // Setters and Getters
  // -------------------
  public void setWidth(double newWidth) {
    if (newWidth > 0)
      this.width = newWidth;
  }
  
  public double getWidth() {
    return this.width;
  }
  
  public void setSpeed(double newSpeed) {
    if (newSpeed > 0)
      this.speed = newSpeed;
  }
  
  public double getSpeed() {
    return this.speed;
  }
  // -------------------
  // Setters and Getters
  // --------END--------
  
}
