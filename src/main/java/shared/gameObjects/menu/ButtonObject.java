package shared.gameObjects.menu;

import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;

public abstract class ButtonObject extends GameObject {

  protected transient Button button;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonObject(
      double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
    button = new Button("", imageView);
  }

  @Override
  public void interpolatePosition(float alpha) {}

  public void doOnClick(MouseEvent e) {
    animation.switchAnimation("clicked");
  }

  public void doOnUnClick(MouseEvent e) {
    animation.switchDefault();
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    button = new Button("", imageView);
    button.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
    root.getChildren().add(button);
    button.setOnMousePressed(event -> doOnClick(event));
    button.setOnMouseReleased(event -> doOnUnClick(event));
  }

  public void initialiseAnimation(String unclickedPath, String clickedPath) {
    this.animation.supplyAnimation("default", unclickedPath);
    this.animation.supplyAnimation("clicked", clickedPath);
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void update() {
    super.update();
  }

  @Override
  public void render() {
    super.render();
    button.relocate(getX(), getY());
  }

  public Button getButton() {
    return button;
  }
}
