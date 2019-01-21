package shared.gameObjects.objects.Components;

import shared.gameObjects.GameObject;

import java.io.Serializable;

/**
 * @author fxa579
 * Base class for Components, all GameObject components extend this class
 */
public abstract class Component implements Serializable {
    protected boolean isActive;
    protected GameObject parent;
    protected ComponentType type;

    public void setIsActive(boolean state) {
        isActive = state;
    }
    public boolean isActive() {
        return isActive;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public ComponentType getType() {
        return type;
    }

    public void update() {

    }
}
