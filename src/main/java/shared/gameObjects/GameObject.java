package shared.gameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Transform;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.util.maths.Vector2;

public abstract class GameObject implements Serializable {

  protected UUID objectUUID;
  protected ObjectID id;
  
  protected transient ImageView imageView;
  protected transient Group root;

  protected transient Animator animation;

  protected GameObject parent;
  protected Set<GameObject> children;
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
  public GameObject(double x, double y, ObjectID id, UUID objectUUID) {
    this.updated = false;
    this.id = id;
    this.objectUUID = objectUUID;
    active = true;
    this.transform = new Transform(this, new Vector2((float) x, (float) y));
    components = new ArrayList<>();
    children = new HashSet<>();
    parent = null;
    animation = new Animator();
    initialiseAnimation();
  }
  
  // Initialise the animation
  public abstract void initialiseAnimation();

  // Server and Client side
  public void update() {
    animation.update();
  }

  // Client Side only
  public void render() {
    imageView.setImage(animation.getImage());
  }

  //Interpolate Position Client only
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
  public abstract String getState();

  // Ignore for now, added due to unSerializable objects
  public void initialise(Group root) {
    this.root = root;
    animation = new Animator();
    initialiseAnimation();
    imageView = new ImageView();
    root.getChildren().add(this.imageView);
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


  /**
   * Basic Getters and Setters
   */

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

  public GameObject getParent() {
    return parent;
  }

  public void setParent(GameObject parent) {
    this.parent = parent;
  }

  public Set<GameObject> getChildren() {
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

  public boolean isUpdated() {
    return updated;
  }

  public void setUpdated(boolean updated) {
    this.updated = updated;
  }

  public void setActive(boolean state) {
    if (!destroyed) {
      active = state;
    }
  }

  public boolean isDestroyed() {
    return destroyed;
  }
}
