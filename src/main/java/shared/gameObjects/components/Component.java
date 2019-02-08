package shared.gameObjects.Components;

import shared.gameObjects.GameObject;

import java.io.Serializable;
import shared.gameObjects.components.ComponentType;

/** @author fxa579 Base class for components, all GameObject components extend this class */
public abstract class Component implements Serializable {

  private boolean isActive;
  private GameObject parent;
  private ComponentType componentType;

  Component(GameObject parent, ComponentType componentType) {
    this.parent = parent;
    this.componentType = componentType;
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

  public void update() {}
}
