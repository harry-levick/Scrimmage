package shared.gameObjects;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.gameObjects.Components.Component;
import shared.gameObjects.Components.ComponentType;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Transform;
import shared.gameObjects.Utils.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class GameObject implements Serializable {

    protected double x, y;
    protected ObjectID id;

    protected transient Version version;
    protected transient ImageView imageView;
    protected transient Group root;
    protected transient Image baseImage;

    protected GameObject parent;
    protected Set<GameObject> children;
    protected ArrayList<Component> components;
    protected Transform transform;

    protected boolean active;
    protected boolean destroyed;

    /**
     * Base class used to create an object in game.
     * This is used on both the client and server side to ensure actions are calculated the same
     *
     * @param x X coordinate of object in game world
     * @param y Y coordinate of object in game world
     * @param id Unique Identifier of every game object
     */
    public GameObject(double x, double y , ObjectID id) {
        this.x = x;
        this.y = y;
        this.id = id;


        this.transform = new Transform(this);
        components = new ArrayList<>();
        children = new HashSet<>();
        parent = null;
    }

    //Server and Client side
    public abstract void update();

    //Client Side only
    public abstract void render();

    //Ignore for now, added due to unSerializable objects
    public void setupRender(Group root, Image baseImage, Version version) {
        this.root = root;
        imageView = new ImageView();
        this.baseImage = baseImage;
        this.imageView.setImage(baseImage);
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
     *
     * @param type The type of desired component to return
     * @return The first attached component found
     */
    public Component GetComponent(ComponentType type) {
        for (Component c : components) {
            if (c.getType() == type) {
                return c;
            }
        }
        return null;
    }

    /**
     *
     * @param type The type of desired componenet to return
     * @return ArrayList of all found attached components
     */
    public ArrayList<Component> GetComponents(ComponentType type) {
        ArrayList<Component> ret = new ArrayList<>();
        for (Component c : components) {
            if (c.getType() == type) {
                ret.add(c);
            }
        }
        return ret;
    }

    /**
     *
     * @param type The type of desired component to return
     * @return ArrayList of all found attached components to this object and all of its children
     */
    public ArrayList<Component> GetComponentsInChildren(ComponentType type) {
        ArrayList<Component> ret = new ArrayList<>();
        for (Component c : components) {
            if (c.getType() == type) {
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

    //Getters and Setters
    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void setActive(boolean state) {
        if(!destroyed) {
            active = state;
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}


