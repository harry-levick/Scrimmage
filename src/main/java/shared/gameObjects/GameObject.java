package shared.gameObjects;

import client.main.Settings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Transform;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.data.DynamicCollision;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public abstract class GameObject implements Serializable {

  protected UUID objectUUID;
  protected ObjectID id;

  protected Settings settings;

  protected transient ImageView imageView;
  protected transient Group root;
  protected transient Animator animation;
  protected double rotation;

  protected GameObject parent;
  protected ArrayList<GameObject> children;
  protected ArrayList<Component> components;
  protected Transform transform;

  protected boolean active;
  protected boolean destroyed;
  protected boolean updated;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public GameObject(double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    this.updated = false;
    this.id = id;
    this.objectUUID = objectUUID;
    active = true;
    this.transform =
        new Transform(
            this, new Vector2((float) x, (float) y), new Vector2((float) sizeX, (float) sizeY));
    components = new ArrayList<>();
    children = new ArrayList<>();
    parent = null;
    animation = new Animator();
    initialiseAnimation();
  }

  // Initialise the animation
  public abstract void initialiseAnimation();

  // Server and Client side
  public void update() {
    animation.update();
    Collider col = (Collider) getComponent(ComponentType.COLLIDER);
    Rigidbody rb = (Rigidbody) getComponent(ComponentType.RIGIDBODY);
    if (rb != null) {
      rb.update();
    }
    if (col != null) {
      col.update();
    }
    children.forEach(child -> child.update());
  }

  // Client Side only
  public void render() {
    imageView.setImage(animation.getImage());
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
    children.forEach(child -> child.render());
  }

  // Collision engine
  public void updateCollision(ArrayList<GameObject> gameObjects) {
    Collider col = (Collider) getComponent(ComponentType.COLLIDER);
    Rigidbody rb = (Rigidbody) getComponent(ComponentType.RIGIDBODY);
    if (col == null) {
      return;
    }
    if (rb != null) {
      if (rb.getBodyType() == RigidbodyType.STATIC) {
        return;
      } else {
        for (GameObject o : Physics.gameObjects) {
          Collider o_col = (Collider) o.getComponent(ComponentType.COLLIDER);
          Rigidbody o_rb = (Rigidbody) o.getComponent(ComponentType.RIGIDBODY);
          if (o_col != null && o_rb != null) {
            if (Collision.haveCollided(col, o_col)) {
              Physics.addCollision(new DynamicCollision(rb, o_rb));
            }
          } else if (o_col != null) {
            if (Collision.haveCollided(col, o_col)) {
            }
          }
        }
      }
    }
  }

  /**
   * Remove the image from the imageView by setting the image to null
   */
  public void removeRender() {
    if (imageView != null) {
      imageView.setImage(null);
      root.getChildren().remove(imageView);
    }
  }

  // Interpolate Position Client only
  public void interpolatePosition(float alpha) {
    if (!isActive()) {
      return;
    }
    imageView.setTranslateX(alpha * getX() + (1 - alpha) * imageView.getTranslateX());
    imageView.setTranslateY(alpha * getY() + (1 - alpha) * imageView.getTranslateY());
  }

  /**
   * Contains the state of the object for sending over server Only contains items that need sending
   * separate by commas
   *
   * @return State of object
   */
  public String getState() {
    /*
    String s = "X:0:Y:0";
    if(getComponent(ComponentType.RIGIDBODY) != null) {
      s = ((Rigidbody) getComponent(ComponentType.RIGIDBODY)).getVelocity().toString();
    }
    */
    String test =
        objectUUID + ";" + getX() + ";" + getY() + ";" + animation.getName(); // + ";" + s;
    return test;
  }

  public void setState(String data) {
    String[] unpackedData = data.split(";");
    setX(Double.parseDouble(unpackedData[1]));
    setY(Double.parseDouble(unpackedData[2]));
    this.animation.switchAnimation(unpackedData[3]);
    /*
    if(getComponent(ComponentType.RIGIDBODY) != null) {
      ((Rigidbody) getComponent(ComponentType.RIGIDBODY)).setVelocity(new Vector2(unpackedData[4]));
    }
    */
  }

  // Ignore for now, added due to unSerializable objects
  public void initialise(Group root) {
    animation = new Animator();
    initialiseAnimation();
    imageView = new ImageView();
    imageView.setRotate(rotation);
    if (root != null) {
      this.root = root;
      root.getChildren().add(this.imageView);
    }
    if (getComponent(ComponentType.COLLIDER) != null && Physics.showColliders) {
      ((BoxCollider) getComponent(ComponentType.COLLIDER)).initialise(root);
    }
    imageView.setFitHeight(transform.getSize().getY());
    imageView.setFitWidth(transform.getSize().getX());
    children.forEach(child -> {
      child.initialiseAnimation();
      child.initialise(root);
    });
  }

  public void addChild(GameObject child) {
    children.add(child);
  }

  public void removeChild(GameObject child) {
    children.remove(child);
  }

  public boolean isChild(GameObject child) {
    return children.contains(child);
  }

  public void addComponent(Component component) {
    components.add(component);
  }

  public void removeComponent(Component component) {
    components.remove(component);
  }

  /**
   * @param type The type of desired components to return
   * @return The first attached components found
   */
  public Component getComponent(ComponentType type) {
    for (Component c : components) {
      if (c.getComponentType() == type) {
        return c;
      }
    }
    return null;
  }

  /**
   * @param type The type of desired componenet to return
   * @return ArrayList of all found attached components
   */
  public ArrayList<Component> getComponents(ComponentType type) {
    ArrayList<Component> ret = new ArrayList<>();
    for (Component c : components) {
      if (c.getComponentType() == type) {
        ret.add(c);
      }
    }
    return ret;
  }

  /**
   * @param type The type of desired components to return
   * @return ArrayList of all found attached components to this object and all of its children
   */
  public ArrayList<Component> getComponentsInChildren(ComponentType type) {
    ArrayList<Component> ret = new ArrayList<>();
    for (Component c : components) {
      if (c.getComponentType() == type) {
        ret.add(c);
      }
    }
    for (GameObject go : children) {
      ret.addAll(go.getComponentsInChildren(type));
    }
    return ret;
  }

  public void destroy() {
    destroyed = active = false;
  }

  /** Basic Getters and Setters */
  public double getX() {
    return this.transform.getPos().getX();
  }

  public void setX(double x) {
    this.transform.getPos().setX((float) x);
  }

  public double getY() {
    return this.transform.getPos().getY();
  }

  public void setY(double y) {
    this.transform.getPos().setY((float) y);
  }

  public ObjectID getId() {
    return id;
  }

  public UUID getUUID() {
    return objectUUID;
  }

  public GameObject getParent() {
    return parent;
  }

  public void setParent(GameObject parent) {
    this.parent = parent;
  }

  public ArrayList<GameObject> getChildren() {
    return children;
  }

  public List<Component> getComponents() {
    return components;
  }

  public Transform getTransform() {
    return transform;
  }

  public void setTransform(Transform transform) {
    this.transform = transform;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean state) {
    if (!destroyed) {
      active = state;
    }
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  public boolean isUpdated() {
    return updated;
  }

  public void setUpdated(boolean updated) {
    this.updated = updated;
  }

  public boolean isDestroyed() {
    return destroyed;
  }

  public double getRotation() {
    return rotation;
  }

  public void rotateImage(double rotation) {
    imageView.setRotate(imageView.getRotate() + rotation);
  }

  @Override
  public boolean equals(Object obj) {
    try {
      GameObject gameobj = (GameObject) obj;
      return this.objectUUID.equals(gameobj.getUUID());
    } catch (Exception e) {
      return false;
    }
  }
}
