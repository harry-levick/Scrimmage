package shared.gameObjects.menu;

import java.util.UUID;
import javafx.event.EventHandler;
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
  public ButtonObject(double x, double y, ObjectID id,
      String baseImageURL, UUID objectUUID, String clickedImageURL) {
    super(x, y, id, baseImageURL, objectUUID);
    spriteLibaryURL.put("clickedImage", clickedImageURL);
    button = new Button("", imageView);
  }

  @Override
  public void interpolatePosition(float alpha) {

  }

  @Override
  public void initialise(Group root, boolean animate) {
    super.initialise(root, animate);
    button = new Button("", imageView);
    root.getChildren().add(button);
    button.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        imageView.setImage(spriteLibary.get("clickedImage"));
      }
    });

    button.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        imageView.setImage(spriteLibary.get("baseImage"));
      }
    });
    button.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void update() {
  }

  @Override
  public void render() {
    button.relocate(getX(), getY());
  }
}
