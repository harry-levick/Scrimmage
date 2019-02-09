package shared.gameObjects.weapons;

import java.util.UUID;
import client.main.Client;
import javafx.scene.image.Image;
import shared.util.maths.Vector2;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

/**
 * @author hlf764
 */
public class Bullet extends GameObject {

  private static String imagePath = "images/weapons/bullet.png";
  public boolean isHit;     // true if there is an object at that position
  private double width;     // width of bullet
  private double speed;     // speed of bullet
  private Vector2 vector;   // Vector of the force of bullet fire
  private Image bulletImage;// image of the bullet
  private Rigidbody rb = new Rigidbody(RigidbodyType.DYNAMIC, 100f, 100f, 0.1f, new MaterialProperty(0, 0, 0), new AngularData(0, 0, 0, 0), this);

  public Bullet(
      double gunX,          // gun initial x position
      double gunY,          // gun initial y position
      double mouseX,        // mouse initial x position
      double mouseY,        // mouse initial y position
      double width,         // the width of the bullet
      double speed,         // the speed of the bullet
      UUID uuid) {          // uuid of this bullet

    super(gunX, gunY, ObjectID.Bullet, uuid);
    setWidth(width);
    setSpeed(speed);
    
    // Unit vector of the bullet force
    vector = new Vector2((float)(mouseX - gunX), (float)(mouseY - gunY));
    vector = vector.div((float)Math.sqrt(vector.dot(vector)));
    
    addComponent(rb);
    // Change the speed of bullet by altering the bulletSpeed variable in any Gun
    rb.setVelocity(vector.mult((float)speed * 2250f));
    //rb.move(new Vector2((float)(mouseX-gunX)*1.5f, (float)(mouseY-gunY)*1.5f));
    
    this.bulletImage = getImage();
    this.isHit = false;
    
    Client.levelHandler.addGameObject(this);

    render();
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", new Image[]{this.bulletImage}); 
  }

  @Override
  public void update() {
    if (isHit) {
      System.out.println(this.toString() + " is to be destroyed");
      Client.levelHandler.delGameObject(this);
    }
    else if ((0 < getX() && getX() < 1920) && (0 < getY() && getY() < 1080)) {
      rb.update();
      super.update();
    }
    else {
      System.out.println(this.toString() + " is to be destroyed");
      Client.levelHandler.delGameObject(this);
    }
    // if something is in this position (will take width into account later)
    // isHit = true;
    // apply effect (deduct hp, sound, physics)
    // destroy this object
  }

  @Override
  public void render() {
    super.render();
    //imageView.relocate(newX, newY);
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());
  }

  @Override
  public void interpolatePosition(float alpha) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getState() {
    return null;
  }

  // -------START-------
  // Setters and Getters
  // -------------------
  public Image getImage() {
    // generate a bullet image based on bulletWidth
    Image image = new Image(imagePath);
    return image;
  }

  public double getWidth() {
    return this.width;
  }
  
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
  
  public boolean getIsHit() {
    return this.isHit;
  }
  
  public void setIsHit(boolean hit) {
    this.isHit = hit;
  }
  // -------------------
  // Setters and Getters
  // --------END--------

}
