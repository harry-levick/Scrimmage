package shared.gameObjects;

import client.main.Settings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
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
import shared.gameObjects.players.Limb;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.data.DynamicCollision;
import shared.physics.data.SimulatedDynamicCollision;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * Base Class for all objects in a scene
 */
public abstract class GameObject implements Serializable {

  /**
   * UUID to determine identity of object
   */
  protected UUID objectUUID;
  /**
   * The type of object used for server/client purposes
   */
  protected ObjectType id;

  /**
   * The client/server settings this object is attached to
   */
  protected transient Settings settings;

  /**
   * The JavaFX rendering component for images
   */
  protected transient ImageView imageView;
  /**
   * The JavaFX group rendered to
   */
  protected transient Group root;
  /**
   * The animation controller for animating still images
   */
  protected transient Animator animation;

  /**
   * The gameObject this object is attached to; null if no parent exists
   */
  protected GameObject parent;
  /**
   * The gameObjects this object is a parent to
   */
  protected ArrayList<GameObject> children;
  /**
   * The components attached to this gameObject
   */
  protected CopyOnWriteArrayList<Component> components;
  /**
   * The transform component responsible for scaling, position and rotation
   */
  protected Transform transform;

  /**
   * Boolean to determine whether or not a gameObject is active
   */
  protected boolean active;
  /**
   * Boolean to determine if an object is destroyed; once set to true it cannot be set back to
   * false
   */
  protected boolean destroyed;

  //Networking
  //TODO: Networking comments
  protected boolean networkStateUpdate;
  protected Vector2 lastPos;
  protected ArrayList<TimePosition> positionBuffer;

  private ArrayList<GameObject> collidedObjects;
  private ArrayList<GameObject> collidedThisFrame;
  private ArrayList<GameObject> collidedToRemove;

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
    this.settings = new Settings(null, root);
    this.transform = new Transform(this, new Vector2((float) x, (float) y),
        new Vector2((float) sizeX, (float) sizeY));
    this.components = new CopyOnWriteArrayList<>();
    //So update sent by server on first frame
    this.lastPos = new Vector2((float) x + 1, (float) y + 1);
    this.children = new ArrayList<>();
    this.animation = new Animator();
    this.collidedObjects = new ArrayList<>();
    this.collidedThisFrame = new ArrayList<>();
    this.collidedToRemove = new ArrayList<>();
  }

  // Initialise the animation

  /**
   * Allows the setting of a sprite and animator
   */
  public void initialiseAnimation() {

  }

  // Server and Client side

  /**
   * Update loops update all components and updates server with new positions
   */
  public void update() {
    networkStateUpdate = false;
    if (destroyed) {
      return;
    }
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

  /**
   * Renders the game object in the client view with all changes applied
   */
  public void render() {
    imageView.setImage(animation.getImage());
    imageView.setRotate(getTransform().getRot());
    imageView.setTranslateX(getX());
    imageView.setTranslateY(getY());
    imageView.setFitWidth(transform.getSize().getX());
    imageView.setFitHeight(transform.getSize().getY());
  }

  // Collision engine

  /**
   * Method used by collision engine to check for collisions each frame
   */
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
   * Use to only update the Physics of the object being called; used primarily by AI
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
    }
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

  // Interpolate Position Client only
  public void interpolatePosition(float alpha) {
    imageView.setTranslateX(alpha * getX() + (1 - alpha) * imageView.getTranslateX());
    imageView.setTranslateY(alpha * getY() + (1 - alpha) * imageView.getTranslateY());
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


  public void initialise(Group root, Settings settings) {
    this.settings = settings;
    this.positionBuffer = new ArrayList<>();
    this.networkStateUpdate = false;
    animation = new Animator();
    initialiseAnimation();
    imageView = new ImageView();
    imageView.setRotate(transform.getRot());
    if (root != null) {
      this.root = root;
      root.getChildren().add(this.imageView);
    }
    if (getComponent(ComponentType.COLLIDER) != null && Physics.showColliders
        && this instanceof Limb) {
      ((Collider) getComponent(ComponentType.COLLIDER)).initialise(root);
    }
    imageView.setFitHeight(transform.getSize().getY());
    imageView.setFitWidth(transform.getSize().getX());
  }

  /**
   * Sets this gameObject as the parent to a defined gameObject
   *
   * @param child The object to make a child to this object
   */
  public void addChild(GameObject child) {
    children.add(child);
    child.setParent(this);
    settings.getLevelHandler().addGameObject(child);
  }

  /**
   * Removes this gameObject as the parent to a defined gameObject
   *
   * @param child The object to no longer make a child to this object
   */
  public void removeChild(GameObject child) {
    if (children.remove(child)) {
      child.setParent(null);
    }
  }

  /**
   * Checks if this object is a parent to a passed gameObject
   */
  public boolean isChild(GameObject child) {
    return child.parent.equals(this);
  }

  /**
   * Adds a new component to this object
   *
   * @param component Component to add
   */
  public void addComponent(Component component) {
    components.add(component);
  }

  /**
   * Removes a component from this object
   *
   * @param component Component to remove
   */
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
    destroyed = true;
    active = false;
    removeRender();
  }

  /**
   * Basic Getters and Setters
   */
  public double getX() {
    return this.transform.getPos().getX();
  }

  public void setX(double x) {
    this.transform.translate(new Vector2(x, transform.getPos().getY()).sub(transform.getPos()));
  }

  public double getY() {
    return this.transform.getPos().getY();
  }

  public void setY(double y) {
    this.transform.translate(new Vector2(transform.getPos().getX(), y).sub(transform.getPos()));
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

  public CopyOnWriteArrayList<Component> getComponents() {
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
    return transform.getRot();
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

  @Override
  public int hashCode() {
    assert false : "hashCode not designed";
    return 99; // any arbitrary constant will do
  }
}
