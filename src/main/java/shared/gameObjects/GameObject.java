package shared.gameObjects;

import static client.main.Settings.levelHandler;

import client.main.Settings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.Utils.TimePosition;
import shared.gameObjects.Utils.Transform;
import shared.gameObjects.animator.Animator;
import shared.gameObjects.components.Behaviour;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.data.DynamicCollision;
import shared.physics.data.SimulatedDynamicCollision;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public abstract class GameObject implements Serializable {

  protected UUID objectUUID;
  protected ObjectType id;

  protected Settings settings = new Settings();

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

  //Networking
  protected boolean networkStateUpdate;
  protected Vector2 lastPos;
  protected ArrayList<TimePosition> positionBuffer;

  protected ArrayList<GameObject> collidedObjects;
  protected ArrayList<GameObject> collidedThisFrame;
  protected ArrayList<GameObject> collidedToRemove;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public GameObject(double x, double y, double sizeX, double sizeY, ObjectType id,
      UUID objectUUID) {
    this.networkStateUpdate = false;
    this.id = id;
    this.objectUUID = objectUUID;
    this.active = true;
    this.transform = new Transform(this, new Vector2((float) x, (float) y),
        new Vector2((float) sizeX, (float) sizeY));
    this.components = new ArrayList<>();
    //So update sent by server on first frame
    this.lastPos = new Vector2((float) x + 1, (float) y + 1);
    this.children = new ArrayList<>();
    this.animation = new Animator();
    this.collidedObjects = new ArrayList<>();
    this.collidedThisFrame = new ArrayList<>();
    this.collidedToRemove = new ArrayList<>();
    initialiseAnimation();
  }

  // Initialise the animation
  public abstract void initialiseAnimation();

  // Server and Client side
  public void update() {
    networkStateUpdate = false;
    animation.update();

    for (Component comp : getComponents(ComponentType.RIGIDBODY)) {
      if (comp.isActive()) {
        comp.update();
      }
    }

    for (Component comp : getComponents(ComponentType.COLLIDER)) {
      if (comp.isActive()) {
        comp.update();
      }
    }

    for (Component comp : getComponents(ComponentType.BEHAVIOUR)) {
      if (comp.isActive()) {
        comp.update();
      }
    }
    //If objects location has changed then send update if server
    if (!(lastPos.equals(getTransform().getPos()))) {
      networkStateUpdate = true;
    }
    this.lastPos.setVec((float) getX(), (float) getY());

  }

  // Client Side only
  public void render() {
    imageView.setImage(animation.getImage());
    imageView.setRotate(getTransform().getRot());
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());

  }

  // Collision engine
  public void updateCollision() {
    ArrayList<Component> cols = getComponents(ComponentType.COLLIDER);
    Rigidbody rb = (Rigidbody) getComponent(ComponentType.RIGIDBODY);
    for (Component comp : cols) {
      Collider col = (Collider) comp;
      if (col == null) {
        return;
      }
      if (rb != null && !col.isTrigger()) {
        if (rb.getBodyType() == RigidbodyType.STATIC) {
          callCollisionMethods(col, false);
          return;
        } else {
          for (GameObject o : Physics.gameObjects.values()) {
            Collider o_col = (Collider) o.getComponent(ComponentType.COLLIDER);
            Rigidbody o_rb = (Rigidbody) o.getComponent(ComponentType.RIGIDBODY);
            if (o_col != null && o_rb != null) {
              if (Collider.haveCollided(col, o_col) && !o_col.isTrigger()) {
                Physics.addCollision(new DynamicCollision(rb, o_rb));
              }
            }
          }
          callCollisionMethods(col, false);
        }
      } else if (col.isTrigger()) {
        callCollisionMethods(col, true);
      }
    }

  }

  /**
   * Use to only update the Physics of the object being called
   */
  public void simulateUpdateCollision() {
    ArrayList<Component> cols = getComponents(ComponentType.COLLIDER);
    Rigidbody rb = (Rigidbody) getComponent(ComponentType.RIGIDBODY);
    for (Component comp : cols) {
      Collider col = (Collider) comp;
      if (col == null) {
        return;
      }
      if (rb != null && !col.isTrigger()) {
        if (rb.getBodyType() == RigidbodyType.STATIC) {
          return;
        } else {
          for (GameObject o : Physics.gameObjects.values()) {
            Collider o_col = (Collider) o.getComponent(ComponentType.COLLIDER);
            Rigidbody o_rb = (Rigidbody) o.getComponent(ComponentType.RIGIDBODY);
            if (o_col != null && o_rb != null) {
              if (Collider.haveCollided(col, o_col) && !o_col.isTrigger()) {
                new SimulatedDynamicCollision(rb, o_rb);
              }
            }
          }
        }
      }
    }

  }

  private void callCollisionMethods(Collider col, boolean isTrigger) {
    if (!isTrigger) {
      for (GameObject o : Physics.gameObjects.values()) {
        Collider o_col = (Collider) o.getComponent(ComponentType.COLLIDER);
        if (o_col != null) {
          if (Collider.haveCollided(col, o_col)) {
            Collision collision = new Collision(o, col, o_col);
            collidedThisFrame.add(o);
            if (!collidedObjects.contains(o)) {
              OnCollisionEnter(collision);
              collidedObjects.add(o);
            }
            OnCollisionStay(collision);
          }
        }
      }
      for (GameObject o : collidedObjects) {
        if (!collidedThisFrame.contains(o)) {
          collidedToRemove.add(o);
          OnCollisionExit(new Collision(o, col, (Collider) o.getComponent(ComponentType.COLLIDER)));
        }
      }

    } else {
      for (GameObject o : Physics.gameObjects.values()) {
        Collider o_col = (Collider) o.getComponent(ComponentType.COLLIDER);
        if (o_col != null) {
          if (Collider.haveCollided(col, o_col)) {
            Collision collision = new Collision(o, col, o_col);
            collidedThisFrame.add(o);
            if (!collidedObjects.contains(o)) {
              OnTriggerEnter(collision);
              collidedObjects.add(o);
            }
            OnTriggerStay(collision);
          }
        }
      }

      for (GameObject o : collidedObjects) {
        if (!collidedThisFrame.contains(o)) {
          collidedToRemove.add(o);
          OnCollisionExit(new Collision(o, col, (Collider) o.getComponent(ComponentType.COLLIDER)));
        }
      }
    }

    for (GameObject o : collidedToRemove) {
      collidedObjects.remove(o);
    }
    collidedToRemove.clear();
    collidedThisFrame.clear();
  }

  /**
   * Remove the image from the imageView by setting the image to null
   */
  public void removeRender() {
    if (imageView != null) {
      imageView.setImage(null);
      /*
       */

      Platform.runLater(
          () -> {
            root.getChildren().remove(imageView);
          }
      );
      //root.getChildren().remove(imageView);
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
    return objectUUID + ";" + id + ";" + (float) getX() + ";" + (float) getY();
  }

  public void setState(String data, Boolean snap) {
    String[] unpackedData = data.split(";");
    Vector2 statePos = new Vector2(Double.parseDouble(unpackedData[2]),
        Double.parseDouble(unpackedData[3]));
    if (snap) {
      setX(Double.parseDouble(unpackedData[2]));
      setY(Double.parseDouble(unpackedData[3]));
    } else {
      Vector2 difference = statePos.sub(transform.getPos());
      double distance = statePos.magnitude(transform.getPos());

      if (distance > 50) {
        transform.setPos(statePos);
      } else if (distance > 1) {
        transform.setPos((difference.mult(0.5f)).add(getTransform().getPos()));
      }
    }
  }

  // Ignore for now, added due to unSerializable objects
  public void initialise(Group root) {
    this.positionBuffer = new ArrayList<>();
    this.networkStateUpdate = false;
    animation = new Animator();
    initialiseAnimation();
    imageView = new ImageView();
    imageView.setRotate(rotation);
    if (root != null) {
      /*

      Platform.runLater(
          () -> {
            this.root = root;
            root.getChildren().add(this.imageView);
          }
      );
       */
      this.root = root;
      root.getChildren().add(this.imageView);
    }
    if (getComponent(ComponentType.COLLIDER) != null && Physics.showColliders) {
      ((Collider) getComponent(ComponentType.COLLIDER)).initialise(root);
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
    levelHandler.addGameObject(child);
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

  /**
   * Called on the first frame a specific collider is colliding with the object.
   *
   * @param col Collision data of the collision.
   */
  public void OnCollisionEnter(Collision col) {
    for (Component component : getComponents(ComponentType.BEHAVIOUR)) {
      ((Behaviour) component).OnCollisionEnter(col);
    }
  }

  /**
   * Called on the every frame a specific collider is colliding with the object.
   *
   * @param col Collision data of the collision.
   */
  public void OnCollisionStay(Collision col) {
    for (Component component : getComponents(ComponentType.BEHAVIOUR)) {
      ((Behaviour) component).OnCollisionStay(col);
    }
  }

  /**
   * Called on the first frame a specific collider stops colliding with the object.
   *
   * @param col Collision data of the collision.
   */
  public void OnCollisionExit(Collision col) {
    for (Component component : getComponents(ComponentType.BEHAVIOUR)) {
      ((Behaviour) component).OnCollisionExit(col);
    }
  }

  /**
   * Called on the first frame a specific collider is colliding with the object; only works if
   * isTrigger is true.
   *
   * @param col Collision data of the collision.
   */
  public void OnTriggerEnter(Collision col) {
    for (Component component : getComponents(ComponentType.BEHAVIOUR)) {
      ((Behaviour) component).OnTriggerEnter(col);
    }
  }

  /**
   * Called on the every frame a specific collider is colliding with the object; only works if
   * isTrigger is true.
   *
   * @param col Collision data of the collision.
   */
  public void OnTriggerStay(Collision col) {
    for (Component component : getComponents(ComponentType.BEHAVIOUR)) {
      ((Behaviour) component).OnTriggerStay(col);
    }
  }

  /**
   * Called on the first frame a specific collider stops colliding with the object; only works if
   * isTrigger is true.
   *
   * @param col Collision data of the collision.
   */
  public void OnTriggerExit(Collision col) {
    for (Component component : getComponents(ComponentType.BEHAVIOUR)) {
      ((Behaviour) component).OnTriggerExit(col);
    }
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

  public ObjectType getId() {
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

  public boolean isNetworkStateUpdate() {
    return networkStateUpdate;
  }

  public void setNetworkStateUpdate(boolean networkStateUpdate) {
    this.networkStateUpdate = networkStateUpdate;
  }

  public ArrayList<GameObject> getChildren() {
    return children;
  }

  public ArrayList<Component> getComponents() {
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

  public boolean isDestroyed() {
    return destroyed;
  }

  public double getRotation() {
    return rotation;
  }

  public void rotateImage(double rotation) {
    imageView.setRotate(imageView.getRotate() + rotation);
  }

  public ArrayList<TimePosition> getPositionBuffer() {
    return positionBuffer;
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
