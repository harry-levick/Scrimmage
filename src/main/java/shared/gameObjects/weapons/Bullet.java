package shared.gameObjects.weapons;

import client.main.Client;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * @author hlf764
 */
public abstract class Bullet extends GameObject {

  public boolean isHit;     // true if there is an object at that position
  protected Rigidbody rb;
  private double width;     // width of bullet
  private double speed;     // speed of bullet
  private Vector2 vector;   // Vector of the force of bullet fire

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

    // Unit vector of the bullet force
    vector = new Vector2((float) (mouseX - gunX), (float) (mouseY - gunY));
    vector = vector.div((float) Math.sqrt(vector.dot(vector)));
    rb = new Rigidbody(
        RigidbodyType.DYNAMIC,
        100f,
        1,
        0.1f,
        new MaterialProperty(0.1f, 1, 1),
        new AngularData(0, 0, 0, 0),
        this);//TODO FIX
    addComponent(rb);
    // Change the speed of bullet by altering the bulletSpeed variable in any Gun
    rb.setVelocity(vector.mult((float) speed * 2250f));
    //rb.move(new Vector2((float)(mouseX-gunX)*1.5f, (float)(mouseY-gunY)*1.5f));

    this.isHit = false;

    Client.levelHandler.addGameObject(this);

    render();
  }



  @Override
  public void update() {
    if (isHit) {
      System.out.println(this.toString() + " is to be destroyed");
      Client.levelHandler.delGameObject(this);
      // apply effect (deduct hp, play sound)
    } else if ((0 < getX() && getX() < 1920) && (0 < getY() && getY() < 1080)) {
      super.update();
    } else {
      System.out.println(this.toString() + " is to be destroyed");
      Client.levelHandler.delGameObject(this);
    }
  }

  @Override
  public void render() {
    super.render();
    imageView.relocate(getX(), getY());
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
