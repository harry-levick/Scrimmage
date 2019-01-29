package shared.gameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Transform;
import shared.gameObjects.Utils.Version;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.util.maths.Vector2;

public abstract class GameObject implements Serializable {

  protected ObjectID id;
  protected HashMap<String, String> spriteLibaryURL;
  protected boolean animate;

  protected transient Version version;
  protected transient ImageView imageView;
  protected transient Group root;
  protected transient HashMap<String, Image> spriteLibary;

  protected GameObject parent;
  protected Set<GameObject> children;
  protected ArrayList<Component> components;
  protected Transform transform;

  protected boolean active;
  protected boolean destroyed;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public GameObject(double x, double y, ObjectID id, String baseImageURL) {
    spriteLibaryURL = new HashMap<>();
    this.id = id;
    spriteLibaryURL.put("baseImage", baseImageURL);
    animate = false;

    this.transform = new Transform(this, new Vector2((float) x, (float) y));
    components = new ArrayList<>();
    children = new HashSet<>();
    parent = null;
  }

  // Server and Client side
  public abstract void update();

  // Client Side only
  public abstract void render();

  // Ignore for now, added due to unSerializable objects
  public void initialise(Group root, Version version, boolean animate) {
    this.root = root;
    imageView = new ImageView();
    spriteLibary = new HashMap<>();
    // Convert Image URL to Image
    for (Map.Entry<String, String> imageURL : spriteLibaryURL.entrySet()) {
      spriteLibary.put(imageURL.getKey(), new Image(imageURL.getValue()));
    }
    this.animate = animate;

    this.imageView.setImage(spriteLibary.get("baseImage"));
    root.getChildren().add(this.imageView);
    this.version = version;
  }

  public void AddChild(GameObject child) {
    children.add(child);
  }

  public void RemoveChild(GameObject child) {
    children.remove(child);
  }

  public boolean isChild(GameObject child) {
    return children.contains(child);
  }

  public void AddComponent(Component component) {
    components.add(component);
  }

  public void RemoveComponent(Component component) {
    components.remove(component);
  }

  /**
   * @param type The type of desired components to return
   * @return The first attached components found
   */
  public Component GetComponent(ComponentType type) {
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
  public ArrayList<Component> GetComponents(ComponentType type) {
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
  public ArrayList<Component> GetComponentsInChildren(ComponentType type) {
    ArrayList<Component> ret = new ArrayList<>();
    for (Component c : components) {
      if (c.getComponentType() == type) {
        ret.add(c);
      }
    }
    for (GameObject go : children) {
      ret.addAll(go.GetComponentsInChildren(type));
    }
    return ret;
  }

  public void Destroy() {
    destroyed = active = false;
  }

  // Getters and Setters
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

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
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

  public void setActive(boolean state) {
    if (!destroyed) {
      active = state;
    }
  }

  public boolean isDestroyed() {
    return destroyed;
  }
}
