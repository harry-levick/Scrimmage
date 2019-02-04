package shared.gameObjects.menu;

import java.util.UUID;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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
  public ButtonObject(double x, double y, ObjectID id, UUID objectUUID) {
    super(x, y, id, objectUUID);
    button = new Button("", imageView);
  }

  @Override
  public void interpolatePosition(float alpha) {

  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    button = new Button("", imageView);
    root.getChildren().add(button);
    button.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        //this.animation.switchDefault();
      }
    });

    button.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        //this.animation.switchAnimation("clicked");
      }
    });
    button.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
  }
  
  public void initialiseAnimation(String defaultPath,String clickedPath) {
    this.animation.supplyAnimation("default", new Image[]{new Image(defaultPath)}); 
    this.animation.supplyAnimation("clicked", new Image[]{new Image(clickedPath)}); 
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
}
