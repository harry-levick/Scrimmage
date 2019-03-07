package shared.gameObjects.components;

import java.io.Serializable;
import shared.gameObjects.GameObject;

/**
 * @author fxa579 Base class for components, all GameObject components extend this class
 */
public abstract class Component implements Serializable {

  private boolean isActive;
  protected GameObject parent;
  protected ComponentType componentType;

  Component(GameObject parent, ComponentType componentType) {
    this.parent = parent;
    this.componentType = componentType;
    isActive = true;
  }

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

  public ComponentType getComponentType() {
    return componentType;
  }

  public void update() {
  }
}
