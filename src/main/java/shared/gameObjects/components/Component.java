package shared.gameObjects.components;

import java.io.Serializable;
import shared.gameObjects.GameObject;

/** @author fxa579 Base class for components, all GameObject components extend this class */
public abstract class Component implements Serializable {

  protected GameObject parent;
  protected ComponentType componentType;
  private boolean isActive;

  /**
   * Base parent constructor for components
   * @param parent The object the component is attached to
   * @param componentType The type of component
   */
  Component(GameObject parent, ComponentType componentType) {
    this.parent = parent;
    this.componentType = componentType;
    isActive = true;
  }

  /**
   *
   * @param state Sets the active state of the component
   */
  public void setIsActive(boolean state) {
    isActive = state;
  }

  /**
   *
   * @return If the component is active or not
   */
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
